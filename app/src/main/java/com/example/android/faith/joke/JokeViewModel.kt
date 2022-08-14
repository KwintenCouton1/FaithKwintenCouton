package com.example.android.faith.joke

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.faith.network.JokeApi
import com.example.android.faith.network.JokeProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class JokeApiStatus {LOADING, ERROR, DONE}


class JokeViewModel : ViewModel(){
    private val _status = MutableLiveData<JokeApiStatus>()
    val status: LiveData<JokeApiStatus>
    get() = _status

    private val _joke = MutableLiveData<JokeProperty>()
    val joke : LiveData<JokeProperty>
    get() =_joke

    private var viewModelJob = Job()
    private  val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadJoke()
    }


    fun loadJoke(){
        coroutineScope.launch {
            var getJokeDeferred =
                JokeApi.retrofitService.getJokes()
            try {
                _status.value = JokeApiStatus.LOADING
                var jokeResult = getJokeDeferred.await()
                _status.value = JokeApiStatus.DONE
                _joke.value = jokeResult
            } catch (t : Throwable){

                _status.value = JokeApiStatus.ERROR
                _joke.value = null
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
