package com.gb.studentdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class Hod_year : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hod_year)

        findViewById<Button>(R.id.button_first_year).setOnClickListener { help() }
        findViewById<Button>(R.id.button_second_year).setOnClickListener { help() }
        findViewById<Button>(R.id.button_fourth_year).setOnClickListener { help() }

        findViewById<Button>(R.id.button_third_year).setOnClickListener {
            startActivity(Intent(this, Hod_division::class.java))
        }
    }

    private fun help() {
        Toast.makeText(this, "Click on third year", Toast.LENGTH_SHORT).show()
    }
}