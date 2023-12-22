package com.example.uasmobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uasmobileapp.R
import com.example.uasmobileapp.databinding.FragmentDetailsBinding
import com.example.uasmobileapp.db.Film
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Details : BottomSheetDialogFragment(){

    private lateinit var binding : FragmentDetailsBinding

    companion object {
        const val ARG_SELECTED_MOVIE = "ARG_SELECTED_MOVIE"

        fun newInstance(selectedMovie: Film): Details {
            val fragment = Details()
            val args = Bundle().apply {
                putParcelable(ARG_SELECTED_MOVIE, selectedMovie)
            }
            fragment.arguments = args
                return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(layoutInflater)

        // Mendapatkan data dari Intent
//        val judul = requireActivity().intent.getStringExtra("title")
//        val sutradara = requireActivity().intent.getStringExtra("genre")
//        val description = requireActivity().intent.getStringExtra("description")
//        val imgId = requireActivity().intent.getStringExtra("imgId")
//
//        // Menampilkan data di tampilan
//        binding.TxtMovieTitle.text = judul
//        binding.TxtDirector.text = sutradara
//        binding.TxtStoryline.text = description
//
//        // Menggunakan Glide untuk memuat dan menampilkan gambar
//        Glide.with(this)
//            .load(imgId)
//            .skipMemoryCache(true) // Skip caching in memory
//            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
//            .into(binding.imageMovie)

        return binding.root
    }

    // fungsionality
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the movie object from arguments
        val selectedMovie = arguments?.getParcelable<Film>(ARG_SELECTED_MOVIE)

        // Use the selectedMovie object to populate your BottomSheet UI
        if (selectedMovie != null) {
            // Populate your BottomSheet UI with the selected movie details
            // Example: display movie title in a TextView
            val titleTextView: TextView = view.findViewById(R.id.TxtMovieTitle)
            val directorTextView: TextView = view.findViewById(R.id.TxtDirector)
            val storyTextView: TextView = view.findViewById(R.id.TxtStoryline)
            val itemRating: AppCompatRatingBar = view.findViewById(R.id.ratingBar1)
            val image: ImageView = view.findViewById(R.id.imageMovie)

            // ngambil dari parcellable movies
            titleTextView.text = selectedMovie.judul
            directorTextView.text = selectedMovie.sutradara
            storyTextView.text = selectedMovie.deskripsi
            Glide.with(this)
                .load(selectedMovie.imageLink)
                .into(image)
        }
    }



}