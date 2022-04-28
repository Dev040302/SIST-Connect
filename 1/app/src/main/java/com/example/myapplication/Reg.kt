package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_reg.*

class Reg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        var departments = resources.getStringArray(R.array.Departments)

        var arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,departments)
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        depart.adapter=arrayAdapter

btncreate.setOnClickListener {

    if(anomid.text.toString().isEmpty()){
        anomid.setError("ID Required")
        return@setOnClickListener
    }

    var ref= Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
    ref.child("ID").setValue(anomid.text.toString())
    ref.child("Dept").setValue(depart.selectedItem.toString())

    var intent= Intent(this@Reg,Chat::class.java)
    startActivity(intent)

}

    }
}