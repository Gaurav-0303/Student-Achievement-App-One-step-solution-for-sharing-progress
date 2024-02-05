package com.gb.studentdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gb.studentdetails.databinding.ActivityDivisionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DivisionA : AppCompatActivity() {

    private lateinit var binding: ActivityDivisionBinding
    private lateinit var myAdapter: PostAdapter
    private var postList = arrayListOf<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDivisionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myAdapter = PostAdapter(this, postList)
        binding.hodRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.hodRecyclerView.adapter = myAdapter

        Firebase.firestore.collection("POST").get().addOnSuccessListener { it ->
            val tempList = arrayListOf<Post>()
            for(i in it.documents){
                val user = i.toObject<Post>()!!
                tempList.add(user)
            }

            // Sort the temporary list by dateLong in descending order
            tempList.sortWith(compareByDescending { it.dateLong })

            // Clear the original list and add the sorted data
            postList.clear()

            postList.addAll(tempList)
            myAdapter.notifyDataSetChanged()
        }

    }
}