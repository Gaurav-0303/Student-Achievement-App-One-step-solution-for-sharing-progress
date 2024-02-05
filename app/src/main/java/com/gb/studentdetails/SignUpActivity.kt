package com.gb.studentdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.gb.studentdetails.databinding.ActivitySignUpBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for blue color of Sign In
        val text = "<font color=#ff000000>Already have an account,</font> <font color=#1E88E5>Sign In ?</font>"
        binding.textViewSignIn.text = Html.fromHtml(text)

        //go to sign in screen
        binding.textViewSignIn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        //when user click on signUp
        binding.buttonSignUp.setOnClickListener { solve() }
    }
    
    private fun isUniqueId(uniqueId: String, callback: (Boolean) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // Check if the uniqueId exists in the database
        databaseReference.child(uniqueId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exists = dataSnapshot.exists()
                callback(exists)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any database error here
                callback(false) // Return false if there was an error
            }
        })
    }

    private fun solve() {
        val name = binding.editTextName.text.toString()
        val emailId = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val uniqueId = binding.editTextUniqueId.text.toString()

        if(name.isEmpty() || emailId.isEmpty() || password.isEmpty() || uniqueId.isEmpty()){
            Toast.makeText(this, "Please fill above fields", Toast.LENGTH_LONG).show()
        }
        else{
            isUniqueId(uniqueId) { exists ->
                if (exists) {
                    //if user already exist
                    Toast.makeText(this, "Unique ID already exists", Toast.LENGTH_LONG).show()
                } else {
                    val user = User(name, emailId, password, uniqueId)
                    database = FirebaseDatabase.getInstance().getReference("Users")
                    database.child(uniqueId).setValue(user).addOnSuccessListener {
                        binding.editTextName.text?.clear()
                        binding.editTextEmail.text?.clear()
                        binding.editTextPassword.text?.clear()
                        binding.editTextUniqueId.text?.clear()
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "User not registered", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}