package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFavoritePosts (
    @Embedded val user : User,
    @Relation(
        parentColumn = "authId",
        entityColumn = "postId",
    associateBy = Junction(UserFavoriteCrossRef::class)
    )
    val favoritePosts : List<Post>,
        )


