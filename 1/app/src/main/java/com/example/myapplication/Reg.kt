package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_reg.*

class Reg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        var departments = resources.getStringArray(R.array.Departments)

        var arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,departments)
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        depart.adapter=arrayAdapter



    }
}