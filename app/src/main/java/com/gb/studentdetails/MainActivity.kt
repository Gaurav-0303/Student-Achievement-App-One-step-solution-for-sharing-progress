package com.gb.studentdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.gb.studentdetails.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for blue color of Sign Up
        val text = "<font color=#ff000000>Don't have an account,</font> <font color=#1E88E5>Sign Up ?</font>"
        binding.textViewRegister.text = Html.fromHtml(text)

        //go to sign up screen
        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        //when you click on sign in
        binding.buttonSignIn.setOnClickListener {
            val uniqueId = binding.editTextId.text.toString()
            if(uniqueId.isNotEmpty()){
                solve(uniqueId)
            }
            else{
                Toast.makeText(this, "Enter ID", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun solve(uniqueId: String) {

        //for HOD login
        if(binding.editTextId.text.toString() == "0" && binding.editTextPassword.text.toString() == "0"){
            startActivity(Intent(this, Hod_year::class.java))
        }
        else{
            //access user from realtime database
            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child("$uniqueId").get().addOnSuccessListener {
                if(it.exists()){
                    val name = it.child("name").value.toString()
                    val email = it.child("email").value.toString()
                    val password = it.child("password").value.toString()
                    val userId = it.child("uniqueId").value.toString()
                    val enteredPassword = binding.editTextPassword.text.toString()

                    //pass data to student dashboard
                    val intentUser = User(name, email, password, userId)
                    val intent = Intent(this, IndividualStudent::class.java)
                    intent.putExtra("intentUser", intentUser)

                    if(enteredPassword == password){
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}