package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.passwordtxt
import kotlinx.android.synthetic.main.activity_login.registernotxt
import kotlinx.android.synthetic.main.activity_login.signupbtn
import kotlinx.android.synthetic.main.activity_signup.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signinbtn.setOnClickListener {

            var progressDialog:ProgressDialog = ProgressDialog(this)

            progressDialog.setMessage("In Process");
            progressDialog.show();

            var mail:String=" "

            if(registernotxt.text.equals(null)){
                registernotxt.setError("Register Number Required")
                return@setOnClickListener
            }

            if(passwordtxt.text.equals(null)){
                passwordtxt.setError("Password Required")
                return@setOnClickListener
            }

            var ref=Firebase.database.reference.child("Reg_No").child(registernotxt.text.toString())

            ref.addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(!snapshot.value.toString().isEmpty()){
                        movenxt(snapshot.value.toString(),passwordtxt.text.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })

        }

        signupbtn.setOnClickListener {

            var intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)

        }
    }

    private fun movenxt(mail: String, pass: String) {
        var auth=Firebase.auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener{

            var intent = Intent(this,Feeds::class.java)
            startActivity(intent)

        }
    }
}