package com.example.android.faith.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CoachCoachingUsers(
    @Embedded val post : User,
    @Relation(
        parentColumn = "coachId",
        entityColumn = "authId",
        associateBy = Junction(UserCoachCrossRef::class)
    )
    val coaches: List<User>
)
