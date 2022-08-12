package com.example.android.faith.post.link

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.faith.database.Link
import com.example.android.faith.database.Post
import com.example.android.faith.databinding.LinkViewBinding
import com.example.android.faith.post.PostAdapter

class LinkAdapter: ListAdapter<Link, LinkAdapter.LinkViewHolder>(LinkDiffCallback()){

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        return LinkViewHolder.from(parent)
    }

    class LinkViewHolder private constructor(val binding : LinkViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Link){
            binding.link = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): LinkViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LinkViewBinding.inflate(layoutInflater,parent, false)

                return LinkViewHolder(binding)
            }
        }
    }
}

class LinkDiffCallback : DiffUtil.ItemCallback<Link>(){
    override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean {
        return oldItem.linkId == newItem.linkId
    }

    override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
        return oldItem == newItem
    }

}