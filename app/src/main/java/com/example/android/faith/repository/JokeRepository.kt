package com.example.android.faith.repository

import androidx.lifecycle.LiveData
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.joke.DatabaseJoke
import com.example.android.faith.network.Joke
import com.example.android.faith.network.JokeApi
import com.example.android.faith.network.JokeApiService
import com.example.android.faith.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class JokeRepository(private val database: FaithDatabase) {

    val jokes : LiveData<DatabaseJoke> = database.jokeDao.getJokes()

    suspend fun refreshJokes() {
        withContext(Dispatchers.IO){
            try{
                database.jokeDao.getJokes()
                val joke = JokeApi.retrofitService.getJokes().await()
                database.jokeDao.insertAll(joke.asDatabaseModel())

            }catch (T: Throwable){
                Timber.i(T.message)
            }

        }
    }
}