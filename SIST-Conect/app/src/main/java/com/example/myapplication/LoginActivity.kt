package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.passwordtxt
import kotlinx.android.synthetic.main.activity_login.registernotxt
import kotlinx.android.synthetic.main.activity_login.signupbtn
import kotlinx.android.synthetic.main.activity_signup.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val text = "<font color=#FF000000>Don't have a account? </font><font color=#ffffff>Signup Below</font>"
        signupnote.setText(Html.fromHtml(text))

        signinbtn.setOnClickListener {

            if(registernotxt.text.equals(null)){
                registernotxt.setError("Register Number Required")
                return@setOnClickListener
            }

            if(passwordtxt.text.equals(null)){
                passwordtxt.setError("Password Required")
                return@setOnClickListener
            }

        }

        signupbtn.setOnClickListener {

            var intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)

        }
    }

}