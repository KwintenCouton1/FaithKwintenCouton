package com.example.android.faith.post

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.android.faith.MainActivity

import com.example.android.faith.database.PostWithLinksAndComments
import com.example.android.faith.databinding.PostViewBinding
import com.example.android.faith.post.link.LinkAdapter

class PostAdapter(val clickListener: PostListener) : ListAdapter<PostWithLinksAndComments, PostAdapter.PostViewHolder>(PostDiffCallback()){
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    class PostViewHolder private constructor(val binding: PostViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PostWithLinksAndComments, clickListener: PostListener) {
            binding.postWithLinks = item
            binding.clickListener = clickListener
            val linkAdapter = LinkAdapter()

            binding.postLinksRecyclerView.adapter = linkAdapter

            linkAdapter.submitList(item.links)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PostViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = PostViewBinding.inflate(layoutInflater, parent, false)
                return PostViewHolder(binding)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<PostWithLinksAndComments>(){
    override fun areItemsTheSame(oldItem: PostWithLinksAndComments, newItem: PostWithLinksAndComments): Boolean {
        return oldItem.post.postId == newItem.post.postId
    }

    override fun areContentsTheSame(oldItem: PostWithLinksAndComments, newItem: PostWithLinksAndComments): Boolean {
        return oldItem == newItem
    }

}

class PostListener( val clickListener: (postId: Long) -> Unit){
    fun onClick(post : PostWithLinksAndComments) = clickListener(post.post.postId)
}
