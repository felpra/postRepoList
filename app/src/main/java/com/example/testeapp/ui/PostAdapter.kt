package com.example.testeapp.ui

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.testeapp.R
import com.example.testeapp.databinding.ItemPostBinding
import com.example.testeapp.model.Post
import com.example.testeapp.model.PostWithUser
import java.io.Serializable

class PostAdapter(private val onPostItemClickListener: OnPostItemClickListener) : ListAdapter<PostWithUser, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostWithUser) {
            binding.titleTextView.text = post.full_name
            binding.user.text = "Author: " + post.owner.login
            binding.forks.text = "Forks: " + post.forks
            binding.favoriteButton.setImageResource(if (post.isFavorite) R.drawable.filled_star else R.drawable.star_outlined)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.favoriteButton.setOnClickListener {
            item.isFavorite = !item.isFavorite
            holder.binding.favoriteButton.setImageResource(if (item.isFavorite) R.drawable.filled_star else R.drawable.star_outlined)
            onPostItemClickListener.onFavoriteClicked(item)
        }

        holder.itemView.setOnLongClickListener {
            val newList = currentList.toMutableList().apply {
                removeAt(holder.adapterPosition)
            }
            submitList(newList)

            true
        }

        Glide.with(holder.itemView.context)
            .load(item.owner.avatar_url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.userImage)

    }

    class PostDiffCallback : DiffUtil.ItemCallback<PostWithUser>() {
        override fun areItemsTheSame(oldItem: PostWithUser, newItem: PostWithUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostWithUser, newItem: PostWithUser): Boolean {
            return oldItem == newItem
        }
    }

}

interface OnPostItemClickListener {
    fun onFavoriteClicked(post: PostWithUser)
}