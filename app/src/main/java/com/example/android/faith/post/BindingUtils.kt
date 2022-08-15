package com.example.android.faith.post

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.faith.R
import com.example.android.faith.database.Comment
import com.example.android.faith.database.PostWithLinksAndComments
import com.example.android.faith.joke.JokeApiStatus
import timber.log.Timber


@BindingAdapter("postText")
fun TextView.setPostText(item: PostWithLinksAndComments?){
    item?.let {
        Timber.i(item.post.text)
        text = item.post.text
    }
}

@BindingAdapter("postLinks")
fun TextView.setLinkStrings(item: PostWithLinksAndComments?){
    item?.let{
        var linkString = ""

        linkString = item.links.joinToString("\n", transform = {it.linkString} )
        text = linkString
        Timber.i(linkString)
    }

}

@BindingAdapter("jokeText")
fun TextView.setJokeText(item: String?){
    item?.let {
        text = item
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let{
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)

    }
}

@BindingAdapter("jokeApiStatus")
fun bindStatus(statusImageView: ImageView, status : JokeApiStatus?){
    when (status){
        JokeApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        JokeApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        JokeApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("commentText")
fun TextView.bindCommentText(comment: Comment?){
    comment?.let{
        text = it.text
    }

}