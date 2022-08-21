package com.example.android.faith.post.link

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.faith.database.post.Link

@BindingAdapter("linkText")
fun TextView.setLinkText(item: Link){
    item?.let{
        text = it.linkString
    }
}