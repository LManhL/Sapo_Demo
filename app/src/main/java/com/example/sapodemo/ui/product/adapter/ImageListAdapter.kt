package com.example.sapodemo.ui.product.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sapodemo.R
import com.example.sapodemo.presenter.model.Image

class ImageListAdapter(private val images: List<Image>): RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_list, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }
    class ImageViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        private val imageView: ImageView = itemView.findViewById(R.id.ivImageListProductImage)
        fun bind(image: Image){
            Glide.with(itemView).load(image.fullPath)
                .placeholder(R.drawable.ic_blank_photo)
                .fallback(R.drawable.ic_blank_photo)
                .error(R.drawable.ic_blank_photo)
                .centerCrop()
                .into(imageView)
        }
    }
}