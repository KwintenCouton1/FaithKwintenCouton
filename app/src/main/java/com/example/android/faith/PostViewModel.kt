package com.example.android.faith

import android.app.Application
import android.graphics.DiscretePathEffect
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.Post
import com.example.android.faith.database.PostDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class PostViewModel(
    val database: PostDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
//    var text : String = ""
//    var links : MutableList<String> = mutableListOf()
//    lateinit var image : Image
//    var linkText : String = ""


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope =  CoroutineScope (Dispatchers.Main + viewModelJob)

    private val newPost = MutableLiveData<Post>()

    private val posts = database.getAll()

    init{
        initializeNewPost()
    }

    private fun initializeNewPost(){
        uiScope.launch {

        }
    }

    private suspend fun getLatestPostFromDatabase(): Post? {
        return withContext(Dispatchers.IO){
            var post = database.getLatest()
            post
        }
    }

    fun onCreatePost(){
        uiScope.launch {
            val postToAdd = Post()
             insert(postToAdd)

            newPost.value = getLatestPostFromDatabase()
        }
    }

    private suspend fun insert(post: Post){
        withContext(Dispatchers.IO){
            database.insert(post)
        }
    }


}