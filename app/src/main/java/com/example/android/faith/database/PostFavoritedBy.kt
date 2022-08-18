package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PostFavoritedBy(
    @Embedded val post : Post,
    @Relation(
        parentColumn = "postId",
        entityColumn = "authId",
        associateBy = Junction(UserFavoriteCrossRef::class)
    )
    val favoritedBy: List<User>
)