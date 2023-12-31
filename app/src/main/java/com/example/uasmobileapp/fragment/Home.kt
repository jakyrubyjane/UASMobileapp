package com.example.uasmobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasmobileapp.adapter.FilmAdapter
import com.example.uasmobileapp.databinding.FragmentHomeBinding
import com.example.uasmobileapp.db.Film
import com.example.uasmobileapp.fragment.Details
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var itemAdapter : FilmAdapter
    private lateinit var itemList : ArrayList<Film>
    private lateinit var executorService: ExecutorService


    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movies")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Ambil nilai email dari data login (menggunakan Firebase Authentication sebagai contoh)
        val userEmail = getLoggedInUserEmail()

        // Set nilai email ke TextView
//        binding.yourUsn.text = userEmail

        recyclerViewItem = binding.MyRecyclerViewGOAT
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(requireActivity())
        executorService = Executors.newSingleThreadExecutor()
        itemList = arrayListOf()
        itemAdapter = FilmAdapter(itemList, movieadminCollectionRef){movie ->
            executorService.execute{
                val bottomSheetFragment = Details.newInstance(movie)
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
            }
        }
        recyclerViewItem.adapter = itemAdapter

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


                Log.d("CrudMovie", "Data retrieved successfully. Size: ${itemList.size}")
            } else {
                Log.d("CrudMovie", "No data found.")
            }
        }

        return binding.root
    }


    private fun setCardViewClickListener(cardView: View, filmId: String) {
        cardView.tag = filmId
        cardView.setOnClickListener {
            openMovieDetails(filmId)
        }
    }

    private fun openMovieDetails(filmId: String) {
        // Buat Intent untuk membuka DetailsMovie Activity
        val intent = Intent(requireContext(), Details::class.java)

        // Kirim data tambahan (jika diperlukan)
        intent.putExtra("filmId", filmId)

        // Mulai DetailsMovie Activity
        requireActivity().startActivity(intent)
    }


    private fun getLoggedInUserEmail(): String {
        // Gantilah dengan logika yang sesuai untuk mendapatkan nilai email setelah login
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.email ?: "user@example.com"
    }
}