package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPostsAndLinksAndComments (
    @Embedded val user : User,
    @Relation(
        entity = Post::class,
        parentColumn = "authId",
        entityColumn = "userId"
        ) val posts : List<PostWithLinksAndComments>
)