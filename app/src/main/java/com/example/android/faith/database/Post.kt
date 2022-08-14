package com.example.android.faith.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true )
    var postId: Long = 0L,

    var childId: Long = 0L,

    var text : String = "",

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    var image : Bitmap?,

    var created : LocalDateTime = LocalDateTime.now(),

//    var links : List<Link>
)