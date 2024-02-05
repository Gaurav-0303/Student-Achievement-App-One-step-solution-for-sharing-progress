package com.gb.studentdetails

import android.net.Uri

data class StudentAchievement(
    val date: String, val certificateName: String, val issuedBy: String, val imageUri: String, val dateLong: Long
)
