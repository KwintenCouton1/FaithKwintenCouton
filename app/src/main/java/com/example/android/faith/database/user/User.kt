package com.example.android.faith.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    var authId: String,

    var profilePicture: ByteArray? = null,

    var userName : String,

    var userType : UserType



)

enum class UserType{
    BEGELEIDER, JONGERE
}


