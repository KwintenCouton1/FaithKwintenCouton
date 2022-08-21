package com.example.android.faith.database.post

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.android.faith.database.user.User
import com.example.android.faith.database.user.UserFavoriteCrossRef

data class UserWithFavoritePosts (
    @Embedded val user : User,
    @Relation(
        parentColumn = "authId",
        entityColumn = "postId",
    associateBy = Junction(UserFavoriteCrossRef::class)
    )
    val favoritePosts : List<Post>,
        )


