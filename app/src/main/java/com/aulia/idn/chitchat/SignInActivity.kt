package com.aulia.idn.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val toolBar: Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolBar)
        supportActionBar!!.title = getString(R.string.login)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        btn_login.setOnClickListener {
            signinUser()
        }
    }

    private fun signinUser() {
        val email: String = et_email_login.text.toString()
        val password: String = et_password_login.text.toString()

        if (email == "") {
            Toast.makeText(
                this, getString(R.string.text_message_email),
                Toast.LENGTH_LONG
            ).show()
        } else if (password == "") {
            Toast.makeText(
                this, getString(R.string.text_message_password),
                Toast.LENGTH_LONG
            ).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this, getString(R.string.text_error_message) + task.exception!!
                            .message.toString(), Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }
}