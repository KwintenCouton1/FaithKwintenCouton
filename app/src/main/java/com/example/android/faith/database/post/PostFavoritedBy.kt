package com.example.android.faith.database.post

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.android.faith.database.user.User
import com.example.android.faith.database.user.UserFavoriteCrossRef

data class PostFavoritedBy(
    @Embedded val post : Post,
    @Relation(
        parentColumn = "postId",
        entityColumn = "authId",
        associateBy = Junction(UserFavoriteCrossRef::class)
    )
    val favoritedBy: List<User>
)