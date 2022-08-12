package com.example.android.faith.post

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.faith.database.Post
import timber.log.Timber


@BindingAdapter("postText")
fun TextView.setPostText(item: Post?){
    item?.let {
        Timber.i(item.text)
        text = item.text
    }
}

@BindingAdapter("postLinks")
fun TextView.setLinkStrings(item: Post?){
    item?.let{
        var linkString = ""

        item.links.forEach{link: String ->
            linkString += link
        }
        text = linkString
        Timber.i(linkString)
    }

}