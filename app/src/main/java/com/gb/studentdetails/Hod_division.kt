package com.gb.studentdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class Hod_division : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hod_division)

        findViewById<Button>(R.id.button_division_a).setOnClickListener {
            startActivity(Intent(this, DivisionA::class.java))
        }

        findViewById<Button>(R.id.button_division_b).setOnClickListener { help() }
        findViewById<Button>(R.id.button_division_c).setOnClickListener { help() }
    }

    private fun help() {
        Toast.makeText(this, "Click on A division", Toast.LENGTH_SHORT).show()
    }
}