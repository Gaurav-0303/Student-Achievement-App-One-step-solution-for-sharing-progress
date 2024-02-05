package com.gb.studentdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gb.studentdetails.databinding.IndividualStudentRecordBinding

class StudentRecordAdapter(val context: Context, val studentAchievementList: ArrayList<StudentAchievement>): RecyclerView.Adapter<StudentRecordAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: IndividualStudentRecordBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualStudentRecordBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return studentAchievementList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.textViewCertificateName.text = studentAchievementList[position].certificateName
        holder.binding.textViewIssuedBy.text = studentAchievementList[position].issuedBy
        holder.binding.textViewDate.text = studentAchievementList[position].date
        studentAchievementList[position].imageUri.let { uri ->
            Glide.with(context)
                .load(uri)
                .apply(RequestOptions.centerCropTransform()) // To make it a circle
                .into(holder.binding.certificateImage)
        }
    }
}