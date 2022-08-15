package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Relation


data class PostWithLinksAndComments (
    @Embedded val post : Post,
    @Relation(
        parentColumn = "postId",
    entityColumn = "postId")
    val links : List<Link>,
    @Relation(
        parentColumn = "postId",
        entityColumn = "postId")
    val comments : List<Comment>
    )
