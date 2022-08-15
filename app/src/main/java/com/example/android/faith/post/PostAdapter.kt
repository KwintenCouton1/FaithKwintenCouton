package com.example.android.faith.post

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

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

///**
// * [RecyclerView.Adapter] that can display a [PlaceholderItem].
// * TODO: Replace the implementation with code for your data type.
// */
//class PostAdapter(
//    private val values: List<PlaceholderItem>
//) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        return ViewHolder(
//            FragmentPostBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = values[position]
//        holder.idView.text = item.id
//        holder.contentView.text = item.content
//    }
//
//    override fun getItemCount(): Int = values.size
//
//    inner class ViewHolder(binding: FragmentPostBinding) : RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
//        val contentView: TextView = binding.content
//
//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
//    }
//
//}