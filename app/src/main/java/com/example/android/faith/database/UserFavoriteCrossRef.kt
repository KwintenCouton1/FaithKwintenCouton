package com.example.android.faith.database

import androidx.room.Entity

@Entity(primaryKeys = ["authId", "postId"])
data class UserFavoriteCrossRef(
    val authId : String,
    val postId : Long
)
