package com.example.uasmobileapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uasmobileapp.R
import com.example.uasmobileapp.activity.EditFilmActivity
import com.example.uasmobileapp.db.Film
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.makeramen.roundedimageview.RoundedImageView

class AdminAdapter(
    private val itemList: ArrayList<Film>,
    private val movieadminCollectionRef: CollectionReference,
) : RecyclerView.Adapter<AdminAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemList: View) : RecyclerView.ViewHolder(itemList) {
        val title: TextView = itemList.findViewById(R.id.MovieTitle)
        val image : RoundedImageView = itemList.findViewById(R.id.imageMovieDisplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_admin, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.judul


        Glide.with(holder.itemView.context)
            .load(currentItem.imageLink)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.image)

        holder.itemView.findViewById<ConstraintLayout>(R.id.layoutMovie).setOnClickListener {
            val intent = Intent(holder.itemView.context, EditFilmActivity::class.java)
            intent.putExtra("title", currentItem.judul)
            intent.putExtra("genre", currentItem.sutradara)
            intent.putExtra("imgId", currentItem.imageLink)
            intent.putExtra("description", currentItem.deskripsi)
            intent.putExtra("docId", currentItem.docId)

            holder.itemView.context.startActivity(intent)
        }


        holder.itemView.findViewById<ImageView>(R.id.hapus).setOnClickListener {
            deleteItem(currentItem.docId)
        }
    }

    private fun deleteItem(docId: String )  {
        // Delete the document from Firestore
        movieadminCollectionRef.document(docId)
            .delete()
            .addOnSuccessListener {

                // After successful deletion, listen for updates
                movieadminCollectionRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
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
                        notifyDataSetChanged() // Update the RecyclerView
                        Log.d("MovieAdapter", "Data retrieved successfully. Size: ${itemList.size}")
                    } else {
                        itemList.clear()
                        notifyDataSetChanged() // Update the RecyclerView for an empty list
                        Log.d("MovieAdapter", "No data found.")
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.e("MovieAdapter", "Error deleting document: $e")
            }
    }

}