package com.example.myapplication

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.registernotxt
import kotlinx.android.synthetic.main.activity_signup.signupbtn
import android.R.attr.data
import android.app.Activity
import android.net.Uri


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupbtn.setOnClickListener {

            if (registernotxt.text.equals(null)) {
                registernotxt.setError("Register Number Required")
                return@setOnClickListener
            }

            if (passwordtxt.text.equals(null)) {
                passwordtxt.setError("Password Required")
                return@setOnClickListener
            }

            if (nametxt.text.equals(null)) {
                nametxt.setError("Name Required")
                return@setOnClickListener
            }

            if (mailtxt.text.equals(null)) {
                mailtxt.setError("Email Required")
                return@setOnClickListener
            }
        }

        editimgtxt.setOnClickListener {

            var intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200)

        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode === RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode === 200) {
                    // Get the url of the image from data
                    var selectedImageUri = data.data
                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        profileimg.setImageURI(selectedImageUri)
                    }
                }
            }

        }
    }
}
