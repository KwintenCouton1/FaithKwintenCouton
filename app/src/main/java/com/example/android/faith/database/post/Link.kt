package com.example.android.faith.database.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link_table")
data class Link(
    @PrimaryKey(autoGenerate = true)
    var linkId : Long = 0L,

    var postId : Long = 0L,

    var linkString : String

)
