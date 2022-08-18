package com.example.android.faith.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.faith.database.Link
import com.example.android.faith.database.Post
import com.example.android.faith.database.PostDatabaseDao
import kotlinx.coroutines.*

class CreatePostViewModel(
    val postDatabaseDao: PostDatabaseDao,
    val postKey: Long = 0L,
    application: Application
) : AndroidViewModel(application){
    private var viewModelJob = Job()

    private val _post = postDatabaseDao.get(postKey)
    val post
    get() = _post


    private val _navigateToPostDetail = MutableLiveData<Long?>()
    val navigateToPostDetail
    get() = _navigateToPostDetail

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init{

    }

    fun onCreatePost(post: Post, links: List<Link>){
        uiScope.launch {
            insert(post, links)
        }

    }

    private suspend fun insert(post: Post, links: List<Link>){
        withContext(Dispatchers.IO){
            var postId = postDatabaseDao.insertPost(post)

            postDatabaseDao.deleteLinksOfPost(postId)
            links.forEach{
                postDatabaseDao.insertLink(Link(linkId = it.linkId, postId = postId, linkString = it.linkString))
            }
        }
    }

    fun onPostAdded(postId: Long){
        _navigateToPostDetail.value = postId
    }

    fun onPostDetailNavigated(){
        _navigateToPostDetail.value = null
    }

}