package com.example.android.faith.post.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.faith.database.post.Comment
import com.example.android.faith.databinding.CommentViewBinding

class CommentAdapter(
    val clickListenerAdd: AddCommentListener,
    val clickListenerReactions: ReactionsListener,
    val clickListenerPopup: PopupListener,
    val userId : String
    ): ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()){
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position)!!, clickListenerAdd, clickListenerReactions, clickListenerPopup, userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }


    class CommentViewHolder private constructor(val binding: CommentViewBinding): RecyclerView.ViewHolder(binding.root)
        //, View.OnCreateContextMenuListener
    {
        fun bind(item : Comment,
                 clickListenerAdd: AddCommentListener,
                 clickListenerReactions: ReactionsListener,
                 clickListenerPopup: PopupListener,
                 userId: String){
            binding.comment = item
            binding.newComment = Comment(
                postId = item.postId,
                reactionToCommentId = item.commentId,
                text = "",
                userId = userId
                )
            binding.clickListenerAdd = clickListenerAdd
            binding.clickListenerReactions = clickListenerReactions
            binding.clickListenerPopup =  clickListenerPopup
            binding.popupButton.isVisible = userId == item.userId

        }


        companion object{
            fun from(parent: ViewGroup):CommentViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentViewBinding.inflate(layoutInflater, parent, false)
                return CommentViewHolder(binding)
            }
        }

//        override fun onCreateContextMenu(
//            contextMenu: ContextMenu?,
//            view: View?,
//            menuInfo: ContextMenu.ContextMenuInfo?
//        ) {
//            TODO("Not yet implemented")
//        }
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
        return oldItem.text == newItem.text
    }


}

class AddCommentListener(val clickListener: (newComment : Comment) -> Unit){
    fun onClick(comment : Comment) = clickListener(comment)

}

class ReactionsListener(val clickListener : (commentId: Long) -> Unit){
    fun onClick(commentId : Long) = clickListener(commentId)
}

class DeleteCommentListener(val clickListener: (comment : Comment) -> Unit){
    fun onClick(comment: Comment) = clickListener(comment)
}

class EditCommentListener(val clickListener: (comment: Comment) -> Unit ){
    fun onClick(comment: Comment) = clickListener(comment)
}

class PopupListener(val clickListener: (comment: Comment) -> Unit){
    fun onClick(comment: Comment) = clickListener(comment)

}