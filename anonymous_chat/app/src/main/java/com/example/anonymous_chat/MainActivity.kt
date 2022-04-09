package com.example.anonymous_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.anonymous_chat.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    lateinit var aid:String
    lateinit var msg:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        auth = Firebase.auth



        b.btnlog.setOnClickListener{
            var email=b.emailtxt.text.toString()
            var pass=b.passwordtxt.text.toString()
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this){ task ->

                if(task.isSuccessful){

                    var user=FirebaseAuth.getInstance().currentUser

                    if(user?.isEmailVerified == true){
                        Firebase.database.reference.child("Users").addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                aid= snapshot.child(Firebase.auth.currentUser!!.uid).child("ID").getValue().toString()
                                msg= snapshot.child(Firebase.auth.currentUser!!.uid).child("Dept").getValue().toString()
                                var intent=Intent(this@MainActivity,Chat::class.java)
                                intent.putExtra("aid",aid)
                                intent.putExtra("dept",msg)
                                startActivity(intent)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                    else{
                        Toast.makeText(this,"Your Email is Not Verified",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(baseContext, task.exception?.message.toString(),
                        Toast.LENGTH_SHORT).show()
                }

            }
        }
        b.btnsign.setOnClickListener {
            var departments = resources.getStringArray(R.array.Departments)

            var arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,departments)
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

            var alert = AlertDialog.Builder(this)
            var mview = layoutInflater.inflate(R.layout.alertbox,null)
            var semail:EditText = mview.findViewById(R.id.semail)
            var spass:EditText=mview.findViewById(R.id.spassword)
            var sanomid:EditText = mview.findViewById(R.id.anomid)
            var sdept:Spinner = mview.findViewById(R.id.depart)

            sdept.adapter=arrayAdapter

            var createbtn:Button = mview.findViewById(R.id.btncreate)

            alert.setView(mview)
            var alertDialog = alert.create()
            alertDialog.setCanceledOnTouchOutside(false)

            createbtn.setOnClickListener {

                var email=semail.text.toString().trim()
                var pass=spass.text.toString().trim()
                var id=sanomid.text.toString().trim()


                if(email.isEmpty()){
                    semail.setError("Email Required")
                    return@setOnClickListener
                }
                if(pass.isEmpty()){
                    spass.setError("Password Required")
                    return@setOnClickListener
                }
                if(id.isEmpty()){
                    sanomid.setError("ID Required")
                    return@setOnClickListener
                }


                alertDialog.dismiss()

                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){ task ->

                    if(task.isSuccessful){
                        var user=FirebaseAuth.getInstance().currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
                            if(task.isSuccessful){
                                Toast.makeText(this,"Verification Email has been send to your mail id\nVerify you Email and Login",Toast.LENGTH_LONG).show()
                                var ref=Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
                                ref.child("ID").setValue(id)
                                ref.child("Dept").setValue(sdept.selectedItem.toString())
                            }
                        }
                    }
                    else{
                        Toast.makeText(baseContext, task.exception?.message.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            alertDialog.show()


        }

    }



}


