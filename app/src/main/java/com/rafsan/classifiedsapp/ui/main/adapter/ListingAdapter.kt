package com.rafsan.classifiedsapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rafsan.classifiedsapp.R
import com.rafsan.classifiedsapp.data.model.Result
import com.rafsan.classifiedsapp.databinding.ListingItemBinding
import com.rafsan.image_lib.ImageLoader

class ListingAdapter(loader: ImageLoader) :
    RecyclerView.Adapter<ListingAdapter.ItemViewHolder>() {

    private var imageLoader = loader

    inner class ItemViewHolder(val binding: ListingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ListingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        with(holder) {
            item.image_urls_thumbnails?.let { thumbs ->
                if (thumbs.isNotEmpty()) {
                    val imageUrl = thumbs[0]
                    imageLoader.downloadImage(imageUrl, binding.itemThumb, R.drawable.placeholder)
                }
            }
            binding.itemName.text = item.name
        }

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}