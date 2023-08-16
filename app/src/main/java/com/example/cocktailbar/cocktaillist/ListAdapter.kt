package com.example.cocktailbar.cocktaillist

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cocktailbar.R
import com.example.cocktailbar.db.Cocktail
import com.example.cocktailbar.inflate

class ListAdapter(private val onClick: (id: Int) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    var items = mutableListOf<Cocktail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(parent.inflate(R.layout.item_cocktail), onClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ListViewHolder(itemView: View, onClick: (id: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.cocktailTextView)
        private val image: ImageView = itemView.findViewById(R.id.cocktailImageView)
        private var currentId: Int? = null
        fun bind(item: Cocktail) {
            currentId = item.id
            title.text = item.title
            Glide.with(itemView).load(item.image).apply(RequestOptions().centerCrop())
                .placeholder(R.drawable.coctail_photo).into(image)
        }

        init {
            itemView.setOnClickListener {
                onClick(currentId!!)
            }
        }
    }
}