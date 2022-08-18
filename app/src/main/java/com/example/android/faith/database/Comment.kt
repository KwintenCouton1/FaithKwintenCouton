package com.example.android.faith.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_table")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    var commentId : Long = 0L,

    var postId : Long = 0L,

    var reactionToCommentId: Long = 0L,

    var userId : String,

    var text : String,

)
