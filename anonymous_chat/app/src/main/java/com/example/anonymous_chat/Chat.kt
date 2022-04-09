package com.example.anonymous_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anonymous_chat.databinding.ActivityChatBinding
import com.example.anonymous_chat.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Chat : AppCompatActivity() {

    lateinit var aid:String
    lateinit var dept:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_chat)

        var i:Intent=intent
        aid= i.getStringExtra("aid")!!
        dept = i.getStringExtra("dept")!!

        val b = ActivityChatBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.recyclerview.layoutManager = LinearLayoutManager(this)

        var ref=Firebase.database.reference.child("Messages").child(dept)

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
                    first.child("id").setValue(dept)
                    first.child("msg").setValue("This Thread is for $dept department")

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        } )



        b.sendbtn.setOnClickListener {
            var ref=Firebase.database.reference.child("Messages").child(dept).push()
            ref.child("id").setValue(aid)
            ref.child("msg").setValue(b.msg.text.toString())
            b.msg.setText("")
        }
    }
}