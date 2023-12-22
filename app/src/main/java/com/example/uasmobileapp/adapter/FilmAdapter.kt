package com.example.uasmobileapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uasmobileapp.R
import com.example.uasmobileapp.db.Film
import com.example.uasmobileapp.fragment.Details
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FilmAdapter (private val itemList : ArrayList<Film>, private val movieadminCollectionRef: CollectionReference,private val onItemClick: (Film) -> Unit) : RecyclerView.Adapter<FilmAdapter.MyViewHolder>() {

    class MyViewHolder(itemList : View) : RecyclerView.ViewHolder(itemList){
        val title : TextView = itemList.findViewById(R.id.MovieTitle)
        val image : ImageView = itemList.findViewById(R.id.imageMovieDisplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_most_watched,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.judul

        Glide.with(holder.itemView.context)
            .load(currentItem.imageLink)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(holder.image)

        holder.itemView.setOnClickListener{
            val position = position
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(itemList[position])
            }

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}