package com.example.android.faith.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserCoachCrossRef(
    @PrimaryKey(autoGenerate = true)
    val relationShipId : Long = 0L,
    val userId : String,
    val coachId : String
)