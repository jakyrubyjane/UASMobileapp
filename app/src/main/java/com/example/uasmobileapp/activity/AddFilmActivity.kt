package com.example.uasmobileapp.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.uasmobileapp.R
import com.example.uasmobileapp.db.Film
import com.example.uasmobileapp.databinding.TambahFilmBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.UUID

class AddFilmActivity : AppCompatActivity() {
    private lateinit var binding: TambahFilmBinding
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movies")

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imageMovie.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TambahFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.ButtonSubmitMovie.setOnClickListener {
            uploadData(imageUri)
        }

        binding.ButtonAddMovieImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun uploadData(imageUri: Uri? = null) {
        val judul: String = binding.EditMovieTitle.text.toString()
        val director: String = binding.EditMovieDirector.text.toString()
        val desc: String = binding.EditMovieDescription.text.toString()

        if (judul.isNotEmpty() && director.isNotEmpty() && desc.isNotEmpty() && imageUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading Data...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            // Generate a unique ID for the document
            val documentId = UUID.randomUUID().toString()

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$documentId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val movie = Film(judul, imageUrl.toString(), desc, director,documentId)
                    // Add the movie data to Firestore
                    movieadminCollectionRef.document(documentId)
                        .set(movie, SetOptions.merge())
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(this, "Adding Data Failed: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Image Upload Failed: $e", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}