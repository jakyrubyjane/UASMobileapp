package com.example.uasmobileapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasmobileapp.adapter.AdminAdapter
import com.example.uasmobileapp.databinding.ActivityMainAdminBinding
import com.example.uasmobileapp.db.Film
import com.example.uasmobileapp.fragment.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var itemAdapter: AdminAdapter
    private lateinit var itemList: ArrayList<Film>
    private lateinit var recyclerViewItem: RecyclerView

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movies")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewItem = binding.MyRecyclerView
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = AdminAdapter(itemList, movieadminCollectionRef)
        recyclerViewItem.adapter = itemAdapter

        with(binding) {
            ButtonAddMovie.setOnClickListener {
                startActivity(Intent(this@MainActivityAdmin, AddFilmActivity::class.java))
            }

            exit.setOnClickListener {
                // Ubah nilai userType menjadi "guest" di SharedPreferences
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("who", "guest")
                editor.putBoolean("isLoggedIn",false)
                editor.apply()

                // Start activity login
                val intent = Intent(this@MainActivityAdmin, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        movieadminCollectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("CrudMovie", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                itemList.clear()
                for (document in snapshot.documents) {
                    val movie = document.toObject(Film::class.java)
                    if (movie != null) {
                        itemList.add(movie)
                    }
                }
                itemAdapter.notifyDataSetChanged()

                Log.d("CrudMovie Dataa", "${snapshot.toString()}")
                Log.d("CrudMovie Dataa", "${snapshot.toString()}")
                Log.d("CrudMovie Dataa", "${itemList.toString()}")
                Log.d("CrudMovie Dataa", "${itemList.toString()}")
                Log.d("CrudMovie", "Data retrieved successfully. Size: ${itemList.size}")

                Log.d("CrudMovie", "Data retrieved successfully. Size: ${itemList.size}")
            } else {
                Log.d("CrudMovie", "No data found.")
            }
        }

    }
}