package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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