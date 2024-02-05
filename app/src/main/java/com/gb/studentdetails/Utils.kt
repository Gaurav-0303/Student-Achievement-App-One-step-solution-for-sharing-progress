package com.gb.studentdetails

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            // Get the download URL of the uploaded image
            it.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                callback(imageUrl.toString()) // Pass the download URL to the callback
            }
        }
        .addOnFailureListener {
            callback(null) // Pass null to indicate image upload failure
        }
}

