package com.example.android.faith.network

import com.squareup.moshi.Json

data class Joke(
    val id : String,

    val icon_url : String,

    val url: String,

    @Json(name = "value")
    val text: String
)