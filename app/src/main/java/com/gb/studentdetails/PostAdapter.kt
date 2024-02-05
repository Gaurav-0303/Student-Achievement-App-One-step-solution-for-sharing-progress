package com.gb.studentdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gb.studentdetails.databinding.IndividualPostBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(val context: Context, private val postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: IndividualPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualPostBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //for profile image
        Firebase.firestore.collection("POST").get().addOnSuccessListener {
            for(i in it.documents) {
                val user = i.toObject<Post>()!!
                if (user.prn == postList[position].prn) {
                    Glide.with(context).load(user.profileImageUri).into(holder.binding.profileImage)
                }
            }
        }

        //for user name
        holder.binding.textViewUserName.text = postList[position].username

        //for prn
        holder.binding.textViewPrn.text = postList[position].prn

        //for date
        holder.binding.textViewDate.text = postList[position].date

        //for certificate name
        holder.binding.textViewCertificateName.text = postList[position].certificateName

        //for issued by
        holder.binding.textViewIssuedBy.text = postList[position].issuedBy

        //for certificate image
        val imageUri = postList[position].postImageUri

        Glide.with(context)
            .load(imageUri)
            .placeholder(R.drawable.loading)
            .apply(RequestOptions.centerCropTransform()) // To make it a circle
            .into(holder.binding.imageViewCertificate)

    }
}
