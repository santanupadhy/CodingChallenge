package com.interview.codingchallenge.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.interview.codingchallenge.R
import com.interview.codingchallenge.ui.AdapterSearchPhoto.UniversalSearchUpdatedViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo.view.*

class AdapterSearchPhoto : RecyclerView.Adapter<UniversalSearchUpdatedViewHolder>() {
    var isLoaderVisible = false

    private val photos: MutableList<Photo> = mutableListOf()

    fun setData(photosList: List<Photo>) {
        photos.clear()
        photos.addAll(photosList)
        notifyDataSetChanged()
    }

    fun addToLast(newList: List<Photo>) {
        val lastIndex = if (photos.isEmpty()) 0 else photos.size - 1
        photos.addAll(newList)
        notifyItemRangeInserted(lastIndex, newList.size)
    }


    fun allClear() {
        photos.clear()
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        val position = photos.size
        notifyItemChanged(position)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = photos.size
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UniversalSearchUpdatedViewHolder {
        val layoutView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo, parent, false)
        return UniversalSearchUpdatedViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: UniversalSearchUpdatedViewHolder, position: Int) {
        holder.bind(photos[position])

    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class UniversalSearchUpdatedViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var comment: ImageView

        init {
            comment = itemView.findViewById(R.id.imageView)
        }

        fun bind(photo: Photo) {
            Picasso.get().load(photo.url)
                .resize(IMAGE_SIDE_PX, IMAGE_SIDE_PX)
                .centerCrop()
                .into(itemView.imageView)
        }

    }

}
const val IMAGE_SIDE_PX = 400
