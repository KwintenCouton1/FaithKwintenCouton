package com.example.android.faith.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField

class Converters {
    @TypeConverter
    public fun stringToListString(value : String): List<String>{
        return value.split(",")
    }
    @TypeConverter
    fun stringListToString(list : List<String>) : String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun longToLocalDateTime(value : Long): LocalDateTime{
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    @TypeConverter
    fun localDateTimeToLong(value: LocalDateTime) : Long{
        return value.getLong(ChronoField.CLOCK_HOUR_OF_DAY)
    }

}