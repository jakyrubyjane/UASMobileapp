package com.example.uasmobileapp.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uasmobileapp.db.Film
import com.example.uasmobileapp.databinding.EditFilmBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditFilmActivity : AppCompatActivity() {
    private lateinit var binding: EditFilmBinding
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

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
        binding = EditFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ButtonAddMovieImage.setOnClickListener {
            getContent.launch("image/*")
        }

        val judul = binding.EditMovieTitle
        val director = binding.EditMovieDirector
        val desc = binding.EditMovieDescription

        judul.setText(intent.getStringExtra("title"))
        director.setText(intent.getStringExtra("genre"))
        desc.setText(intent.getStringExtra("description"))

        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this)
            .load(originalImageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageMovie)

        binding.ButtonConfirmEditMovie.setOnClickListener {
            uploadData(imageUri, judul.text.toString(), director.text.toString(), desc.text.toString())
        }
    }

    private fun uploadData(imageUri: Uri?, updatedTitle: String, updatedDirector: String, updatedDesc: String) {
        val documentId = Uri.parse(intent.getStringExtra("imgId")).lastPathSegment?.removePrefix("images/")

        if (imageUri != null) {
            // Upload new image and update data
            storageReference = FirebaseStorage.getInstance().reference.child("images/$documentId")
            val uploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val updatedMovie = Film(updatedTitle, imageUrl.toString(), updatedDesc, updatedDirector,documentId!!)
                    // Update data in Firestore
                    movieadminCollectionRef.document(documentId!!)
                        .set(updatedMovie)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Update data without uploading a new image
            val originalImageUrl = intent.getStringExtra("imgId")
            val updatedMovie = Film(updatedTitle, originalImageUrl!!, updatedDesc,updatedDirector , documentId!!)
            movieadminCollectionRef.document(documentId!!)
                .set(updatedMovie)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}