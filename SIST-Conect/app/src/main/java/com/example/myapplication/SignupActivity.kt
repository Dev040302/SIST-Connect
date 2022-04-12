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
import android.app.ProgressDialog
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var auth:FirebaseAuth = Firebase.auth

        var departments = resources.getStringArray(R.array.Departments)

        var arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,departments)
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        depart.adapter=arrayAdapter



        signupbtn.setOnClickListener {

            var progressDialog: ProgressDialog = ProgressDialog(this)

            progressDialog.setMessage("In Process");
            progressDialog.show();

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

            var mail=mailtxt.text.toString()
            var password = passwordtxt.text.toString()

            auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this){ task ->

                if(task.isSuccessful) {
                    var user = FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
                        var ref = Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
                        ref.child("Mail").setValue(mail)
                        ref.child("Name").setValue(nametxt.text.toString())
                        ref.child("Reg_no").setValue(registernotxt.text.toString())
                        ref.child("Dept").setValue(depart.selectedItem.toString())

                        ref=Firebase.database.reference.child("Reg_No").child(registernotxt.text.toString())
                        ref.setValue(mail)

                        var intent = Intent(this,Feeds::class.java)
                        startActivity(intent)
                    }
                }

                else{
                    Toast.makeText(this,"Unable to Signup",Toast.LENGTH_SHORT)
                }
            }

        }

        editimgtxt.setOnClickListener {


        }

    }
}
