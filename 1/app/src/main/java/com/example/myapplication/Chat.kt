package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anonymous_chat.ItemAdapter
import com.example.anonymous_chat.ItemsViewModel
import com.example.myapplication.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class Chat : AppCompatActivity() {

    var Dept=""
    var ID=""
    lateinit var b: ActivityChatBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_chat)

        b=ActivityChatBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.recyclerview.layoutManager = LinearLayoutManager(this)


        var auth = FirebaseAuth.getInstance().currentUser!!.uid

        var ref=Firebase.database.reference.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(auth).exists()){
                  Dept=snapshot.child(auth).child("Dept").getValue().toString()
                    ID=snapshot.child(auth).child("ID").getValue().toString()
                    startrecycle()
                }
                else{
                    var intent= Intent(this@Chat,Reg::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        b.sendbtn.setOnClickListener {
            var ref=Firebase.database.reference.child("Messages").child(Dept).push()
            ref.child("id").setValue(ID)
            ref.child("msg").setValue(b.msg.text.toString())
            b.msg.setText("")
        }

    }

    private fun startrecycle() {
        var ref=Firebase.database.reference.child("Messages").child(Dept)

        ref.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = ArrayList<ItemsViewModel>()
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.getChildren()) {
                        var key = d.key.toString()
                        var id=dataSnapshot.child(key).child("id").getValue().toString()
                        var msg = dataSnapshot.child(key).child("msg").getValue().toString()
                        data.add(ItemsViewModel(id,msg))
                    }
                    val adapter = ItemAdapter(data)

                    b.recyclerview.adapter = adapter

                    val adp=b.recyclerview.adapter

                    b.recyclerview.smoothScrollToPosition(adp!!.itemCount-1)
                }
                else{
                    var first=ref.push()
                    first.child("id").setValue(Dept)
                    first.child("msg").setValue("This Thread is for $Dept department")

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        } )

    }
}