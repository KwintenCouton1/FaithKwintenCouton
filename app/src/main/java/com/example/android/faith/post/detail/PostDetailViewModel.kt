package com.example.android.faith.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.faith.database.daos.PostDatabaseDao
import com.example.android.faith.database.post.PostWithLinksAndComments
import com.example.android.faith.database.daos.UserDao
import kotlinx.coroutines.*

class PostDetailViewModel(
    private val postKey: Long = 0L,
    private val userId: String,
    val postDao: PostDatabaseDao,
    val userDao: UserDao
    ): ViewModel() {
    val viewModelJob = Job()

    private val uiScope =  CoroutineScope (Dispatchers.Main + viewModelJob)

    private val post = MediatorLiveData<PostWithLinksAndComments>()

            fun getPost() = post

    init {
        post.addSource(postDao.get(postKey), post::setValue)
    }

    private val _navigateToPostList = MutableLiveData<Boolean?>()

    val navigateToPostList: LiveData<Boolean?>
    get() =_navigateToPostList


    fun onToggleFavorite(){
        uiScope.launch {

            toggleFavorite()

//          newPost.value = getLatestPostFromDatabase()
        }
    }

    private suspend fun toggleFavorite(){
        withContext(Dispatchers.IO){

            post.value?.post?.favorited = !post.value?.post?.favorited!!
            postDao.updatePost((post.value?.post!!))

//        val userFavorites = userDao.getUserFavorites(userId).value
//            if (userFavorites?.any{
//                    it.postId == post.value?.post?.postId}!!){
//                userDao.deleteUserFavoriteCrossRef(UserFavoriteCrossRef(userId, post.value?.post?.postId!!))
//            } else {
//                userDao.insertUserFavoriteCrossRef(UserFavoriteCrossRef(userId, post.value?.post?.postId!!))
//            }

    }
    }

    fun doneNavigating(){
_navigateToPostList.value =null
    }

    fun onClose(){
        _navigateToPostList.value = true
    }

}