package com.example.android.faith.database.joke

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.faith.network.Joke
import com.squareup.moshi.Json

@Entity
data class DatabaseJoke(
    @PrimaryKey
    val id : String,

    val icon_url : String,

    val url: String,

    val text: String
){

}
fun List<DatabaseJoke>.asDomainModel(): List<Joke>{
    return map {
        Joke(
            id = it.id,
            icon_url = it.icon_url,
            url = it.url,
            text = it.text
        )
    }
}