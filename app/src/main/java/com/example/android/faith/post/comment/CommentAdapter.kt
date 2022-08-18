package com.example.android.faith.post.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.faith.database.Comment
import com.example.android.faith.databinding.CommentViewBinding

class CommentAdapter(val clickListenerAdd: AddCommentListener, val commentViewModel: CommentViewModel, val userId : String): ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()){
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position)!!, clickListenerAdd, commentViewModel, userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }


    class CommentViewHolder private constructor(val binding: CommentViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item : Comment, clickListenerAdd: AddCommentListener, commentViewModel: CommentViewModel, userId: String){
            binding.comment = item
            binding.newComment = Comment(
                postId = item.postId,
                reactionToCommentId = item.commentId,
                text = "",
                userId = userId
                )
            binding.clickListenerAdd = clickListenerAdd

            val adapter = CommentAdapter(
                clickListenerAdd
//                AddCommentListener { commentId, postId, text ->
//                var comment = Comment(
//                    postId = item.postId,
//                    reactionToCommentId = commentId,
//                    text = binding.editTextComment.text.toString())
//                commentViewModel.onSubmitComment(comment)
//            }
        , commentViewModel, userId)

            binding.reactions.adapter = adapter
            adapter.submitList(commentViewModel.getCommentsOfPost(item.commentId).value)

        }

        companion object{
            fun from(parent: ViewGroup):CommentViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentViewBinding.inflate(layoutInflater, parent, false)
                return CommentViewHolder(binding)
            }
        }
    }
}

class CommentDiffCallback: DiffUtil.ItemCallback<Comment>(){
    override fun areItemsTheSame(
        oldItem: Comment,
        newItem: Comment
    ): Boolean {
        return oldItem.commentId == newItem.commentId
    }
    override fun areContentsTheSame(
        oldItem: Comment,
        newItem: Comment
    ): Boolean {
        return oldItem == newItem
    }


}

class AddCommentListener(val clickListener: (newComment : Comment) -> Unit){
    fun onClick(comment : Comment) = clickListener(comment)

}