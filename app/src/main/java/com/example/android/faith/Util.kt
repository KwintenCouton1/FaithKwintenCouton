package com.example.android.faith

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


fun Fragment.setActivityTitle(@StringRes id: Int){
    (activity as AppCompatActivity?)?.supportActionBar?.title = getString(id)
}

fun Fragment.setActivityTitle(title: String){
    (activity as AppCompatActivity?)?.supportActionBar?.title = title
}