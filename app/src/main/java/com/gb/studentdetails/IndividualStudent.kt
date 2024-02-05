package com.gb.studentdetails

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gb.studentdetails.databinding.ActivityIndividualStudentBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class IndividualStudent : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualStudentBinding
    private lateinit var user: User
    private lateinit var database: DatabaseReference
    private lateinit var certificateImage: ImageView
    private lateinit var buttonSubmit: Button
    private lateinit var buttonUploadImage: Button
    private var imageUrl: String? = null
    private var profileImageUrl: String? = null
    private lateinit var studentAchievementList: ArrayList<StudentAchievement>
    private lateinit var studentRecordAdapter: StudentRecordAdapter

    private var launchercher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.centerCropTransform()) // To make it a circle
                    .into(certificateImage)

                uploadImage(uri, "Posts") {
                    if (it == null) {

                    } else {
                        imageUrl = it

                        certificateImage.visibility = View.VISIBLE
                        buttonSubmit.visibility = View.VISIBLE
                        buttonUploadImage.visibility = View.GONE
                    }
                }
            }
        }


    private var profileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Glide.with(this).load(uri).apply(RequestOptions.circleCropTransform()).into(binding.profileImage)
                uploadImage(uri, "Profile Images") {
                    if (it == null) {

                    } else {
                        profileImageUrl = it
                        val data = hashMapOf("profileImage" to profileImageUrl)
                        Firebase.firestore.collection("Profile Images")
                            .document(user.uniqueId).set(data)
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        studentAchievementList = arrayListOf()

        //getting data from sign in screen
        user = intent.getSerializableExtra("intentUser") as User

        // Set name at the top
        binding.textViewStudentName.text = user.name

        // By clicking the plus button(to add new record)
        binding.addButton.setOnClickListener { onButtonClick() }

        // To change the profile picture(tap to profile)
        binding.profileImage.setOnClickListener { profileLauncher.launch("image/*") }

        // To update the profile picture (if already exist)
        Firebase.firestore.collection("Profile Images").document(user.uniqueId).get().addOnSuccessListener {
            if(it.exists()){
                val profileImageString = it.getString("profileImage")
                profileImageUrl = profileImageString
                Glide.with(this)
                    .load(profileImageString)
                    .apply(RequestOptions.centerCropTransform()) // To make it a circle
                    .into(binding.profileImage)
            }
        }

        // when click on logout button
        binding.imageViewLogout.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        //show all data in recycler view
        studentRecordAdapter = StudentRecordAdapter(this, studentAchievementList)
        binding.recyclerView.adapter = studentRecordAdapter

        //when you again sign in
        retriveRecords()
    }

    private fun retriveRecords() {
        Firebase.firestore.collection(user.uniqueId).get().addOnSuccessListener { querySnapshot ->
            val tempList = ArrayList<StudentAchievement>() // Create a temporary list for sorting

            for (document in querySnapshot.documents) {
                val data = document.data
                if (data != null) {
                    val date = data["date"] as String
                    val certificateName = data["certificateName"] as String
                    val issuedBy = data["issuedBy"] as String
                    val imageUri = data["imageUri"] as String

                    val dateLong = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date)?.time ?: 0

                    val studentAchievement =
                        StudentAchievement(date, certificateName, issuedBy, imageUri, dateLong)

                    tempList.add(studentAchievement)
                }
            }

            // Sort the temporary list by dateLong in descending order
            tempList.sortWith(compareByDescending { it.dateLong })

            // Clear the original list and add the sorted data
            studentAchievementList.clear()
            studentAchievementList.addAll(tempList)

            // Notify the adapter that the data has changed
            studentRecordAdapter.notifyDataSetChanged()
        }
    }

    private fun showDatePickerDialog(editTextDate: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Set the selected date in the EditText
            val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            editTextDate.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }


    private fun isFilled(
        date: EditText,
        certificateName: EditText,
        issuedBy: EditText
    ): Boolean {
        return !(date.text.isEmpty() || certificateName.text.isEmpty()|| issuedBy.text.isEmpty())
    }

    private fun onButtonClick() {
        //show the dialog box
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.custom_dialog_box, null)

        certificateImage = dialogView.findViewById(R.id.image_view_certificate)
        buttonSubmit = dialogView.findViewById(R.id.button_submit)
        buttonUploadImage = dialogView.findViewById(R.id.button_upload_image)
        certificateImage.visibility = View.GONE
        buttonSubmit.visibility = View.GONE
        builder.setView(dialogView)
        dialog = builder.create()
        dialog.show()

        //for setting the date
        val imageViewDate = dialogView.findViewById<ImageView>(R.id.image_view_date)
        val editTextDate = dialogView.findViewById<EditText>(R.id.edit_text_date)
        imageViewDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
        }
        editTextDate.isFocusable = false

        //for upload image
        dialogView.findViewById<Button>(R.id.button_upload_image).setOnClickListener {
            launchercher.launch("image/*")
        }

        //for save all data into firebase
        buttonSubmit.setOnClickListener {

            //check that all fields are filled or not
            if(isFilled(
                    dialogView.findViewById<EditText>(R.id.edit_text_date),
                    dialogView.findViewById<EditText>(R.id.edit_text_certificate_name),
                    dialogView.findViewById<EditText>(R.id.edit_text_issued_by)
                )
            ){

                val date = editTextDate.text.toString()
                val certificateName = dialogView.findViewById<EditText>(R.id.edit_text_certificate_name).text.toString()
                val issuedBy = dialogView.findViewById<EditText>(R.id.edit_text_issued_by).text.toString()

                val dateLong = SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date)?.time ?: 0

                //save data to database
                val studentAchievement = StudentAchievement(date, certificateName, issuedBy, imageUrl!!, dateLong)

                //store in realtime database
                database = FirebaseDatabase.getInstance().getReference("StudentAchievement")
                database.child(user.uniqueId).child(UUID.randomUUID().toString()).setValue(studentAchievement).addOnSuccessListener {
                    dialog.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(this, "Achievement not registered", Toast.LENGTH_LONG).show()
                }

                //store in firestore database
                Firebase.firestore.collection(user.uniqueId)
                    .document(UUID.randomUUID().toString()).set(studentAchievement)

                //store all achievments in one place for retrieve easily
                val post: Post = Post(
                    profileImageUrl, user.name, certificateName, issuedBy, imageUrl!!, date, user.uniqueId, dateLong
                )
                Firebase.firestore.collection("POST").document(UUID.randomUUID().toString()).set(post).addOnSuccessListener {

                }

                studentAchievementList.add(studentAchievement)
                studentAchievementList.sortWith(compareByDescending { it.dateLong })
                studentRecordAdapter.notifyDataSetChanged()

            }
            else{
                Toast.makeText(this, "Please give all details", Toast.LENGTH_LONG).show()
            }
        }
    }


}