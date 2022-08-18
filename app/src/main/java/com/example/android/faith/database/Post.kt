package com.example.android.faith.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.faith.FaithApplication
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true )
    var postId: Long = 0L,

    var text : String = "",

    var userId : String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image : ByteArray? = null,

    var created : LocalDateTime = LocalDateTime.now(),

    var favorited : Boolean = false,

//    var links : List<Link>
)