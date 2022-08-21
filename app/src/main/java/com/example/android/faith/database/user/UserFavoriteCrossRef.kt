package com.example.android.faith.database.user

import androidx.room.Entity

@Entity(primaryKeys = ["authId", "postId"])
data class UserFavoriteCrossRef(
    val authId : String,
    val postId : Long
)
