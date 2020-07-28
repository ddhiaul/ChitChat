package com.aulia.idn.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private  lateinit var mAuth : FirebaseAuth
    private lateinit var refUsers : DatabaseReference
    private var firebaseUserID : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val toolbar : Toolbar = findViewById(R.id.toolbar_reg)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = getString(R.string.register)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        btn_reg.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val username : String = et_user_name_reg.text.toString()
        val email : String = et_email_reg.text.toString()
        val password : String = et_password_reg.text.toString()

        if(username == ""){
            Toast.makeText(this, getString(R.string.text_message_username),
                Toast.LENGTH_LONG).show()
        }else if(email == ""){
            Toast.makeText(this, getString(R.string.text_message_email),
                Toast.LENGTH_LONG).show()
        }else if (password == ""){
            Toast.makeText(this, getString(R.string.text_message_password),
                Toast.LENGTH_LONG).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance()
                        .reference.child(getString(R.string.text_users)).child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/chitchat-ee18d.appspot.com/o/profile.png?alt=media&token=205beac9-eb2e-4a65-92da-0496c035a1f0"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/chitchat-ee18d.appspot.com/o/cover.jpeg?alt=media&token=e6021525-df1f-49ea-981f-225fdf7c236f"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()

                    refUsers.updateChildren(userHashMap).addOnCompleteListener {
                            task ->
                        if(task.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }

                } else {
                    Toast.makeText(this, getString(R.string.text_error_message)+
                            task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}