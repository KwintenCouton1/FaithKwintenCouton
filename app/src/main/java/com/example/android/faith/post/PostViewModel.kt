package com.example.android.faith.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.faith.database.Link
import com.example.android.faith.database.Post
import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.database.PostWithLinks
import kotlinx.coroutines.*
import timber.log.Timber

public class PostViewModel(
    val database: PostDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val _post = MutableLiveData<Post>()


    val posts : LiveData<List<PostWithLinks>>
    get() = _posts


    val post : LiveData<Post>
    get() = _post

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope =  CoroutineScope (Dispatchers.Main + viewModelJob)

    private val newPost = MutableLiveData<PostWithLinks>()

    private val _posts = database.getAll()

    init{
        initializeNewPost()
    }

    private fun initializeNewPost(){
        uiScope.launch {

        }
    }

    private suspend fun getLatestPostFromDatabase(): PostWithLinks? {
        return withContext(Dispatchers.IO){
            var post = database.getLatest()
            post
        }
    }

    fun onCreatePost(post: Post, links: List<Link>){
        uiScope.launch {

             insert(post, links)

            newPost.value = getLatestPostFromDatabase()
        }
        Timber.i(post.text)
    }

    private suspend fun insert(post: Post, links: List<Link>){
        withContext(Dispatchers.IO){
            var postId = database.insertPost(post)

            links.forEach{
                database.insertLink(Link(postId = postId, linkString = it.linkString))
            }

        }
    }


}