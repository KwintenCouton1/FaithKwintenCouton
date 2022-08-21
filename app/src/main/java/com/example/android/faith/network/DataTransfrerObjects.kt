package com.example.android.faith.network

import androidx.room.Database
import com.example.android.faith.database.joke.DatabaseJoke
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkJokeContainer(val jokes : List<Joke>)


data class Joke(
    val id : String,

    val icon_url : String,

    val url: String,

    @Json(name = "value")
    val text: String
)

fun Joke.asDatabaseModel(): DatabaseJoke{
    return DatabaseJoke(
        id = this.id,
        icon_url = this.icon_url,
        url = this.url,
        text = this.text)
}


fun NetworkJokeContainer.asDatabaseModel(): Array<DatabaseJoke>{
    return jokes.map {
        DatabaseJoke(
            id = it.id,
            icon_url = it.icon_url,
            url = it.url,
            text = it.text
        )
    }.toTypedArray()
}