package com.example.android.faith.post

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.faith.database.Link
import com.example.android.faith.database.Post
import com.example.android.faith.database.PostWithLinks
import timber.log.Timber


@BindingAdapter("postText")
fun TextView.setPostText(item: PostWithLinks?){
    item?.let {
        Timber.i(item.post.text)
        text = item.post.text
    }
}

@BindingAdapter("postLinks")
fun TextView.setLinkStrings(item: PostWithLinks?){
    item?.let{
        var linkString = ""

        linkString = item.links.joinToString("\n", transform = {it.linkString} )
        text = linkString
        Timber.i(linkString)
    }

}