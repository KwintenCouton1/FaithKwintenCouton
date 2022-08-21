package com.example.android.faith.database.user

import androidx.room.Embedded
import androidx.room.Relation
import com.example.android.faith.database.post.Post
import com.example.android.faith.database.post.PostWithLinksAndComments

data class UserWithPostsAndLinksAndComments (
    @Embedded val user : User,
    @Relation(
        entity = Post::class,
        parentColumn = "authId",
        entityColumn = "userId"
        ) val posts : List<PostWithLinksAndComments>
)