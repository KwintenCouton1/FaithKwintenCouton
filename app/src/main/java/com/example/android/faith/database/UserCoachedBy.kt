package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserCoachedBy(
    @Embedded val post : User,
    @Relation(
        parentColumn = "authId",
        entityColumn = "coachId",
        associateBy = Junction(UserCoachCrossRef::class)
    )
    val coachedBy: List<User>
)
