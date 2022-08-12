package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Relation


data class PostWithLinks (
    @Embedded val post : Post,
    @Relation(
        parentColumn = "postId",
    entityColumn = "postId")
    val links : List<Link>
)