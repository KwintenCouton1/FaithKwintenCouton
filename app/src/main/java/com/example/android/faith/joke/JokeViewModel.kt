package com.example.android.faith.joke

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Database
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.joke.DatabaseJoke
import com.example.android.faith.network.JokeApi
import com.example.android.faith.network.Joke
import com.example.android.faith.network.asDatabaseModel
import com.example.android.faith.repository.JokeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class JokeApiStatus {LOADING, ERROR, DONE}


class JokeViewModel(application: Application) : AndroidViewModel(application){
    private val _status = MutableLiveData<JokeApiStatus>()
    val status: LiveData<JokeApiStatus>
    get() = _status

    private var _joke : LiveData<DatabaseJoke>
    val joke : LiveData<DatabaseJoke>
    get() =_joke

    private val database = FaithDatabase.getInstance(application)

    private val jokeRepository = JokeRepository(database)

    private val _jokes = jokeRepository.jokes


    private var viewModelJob = Job()
    private  val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        coroutineScope.launch {
            jokeRepository.refreshJokes()
        }


        _joke = jokeRepository.jokes
    }


    fun loadJoke(){
        coroutineScope.launch {
            jokeRepository.refreshJokes()
            _joke = jokeRepository.jokes

//            var getJokeDeferred =
//                JokeApi.retrofitService.getJokes()
//            try {
//                _status.value = JokeApiStatus.LOADING
//                var jokeResult = getJokeDeferred.await()
//                _status.value = JokeApiStatus.DONE
//                _joke.value = jokeResult.asDatabaseModel()
//            } catch (t : Throwable){
//
//                _status.value = JokeApiStatus.ERROR
//                _joke.value = null
//            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
