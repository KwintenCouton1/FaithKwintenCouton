package com.example.android.faith.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.faith.FaithApplication
import com.example.android.faith.database.*
import kotlinx.coroutines.*
import timber.log.Timber

public class PostViewModel(
    val postDatabaseDao: PostDatabaseDao,
    val userDao : UserDao,
    val currentUserId : String,
    //val postKey : Long,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    //private val _post = postDatabaseDao.get(postKey)

    private val _navigateToPostDetail = MutableLiveData<Long?>()
    val navigateToPostDetail
    get() = _navigateToPostDetail

    //val post : LiveData<PostWithLinksAndComments?>
    //get() = _post

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope =  CoroutineScope (Dispatchers.Main + viewModelJob)

    private var _currentUser = userDao.getUser(currentUserId)
    val currentUser : LiveData<UserWithPostsAndLinksAndComments?>
    get() = _currentUser

    private var _managedPosts : LiveData<List<PostWithLinksAndComments?>> = postDatabaseDao.getPostsByCoachedUsers(currentUserId)

    private var _createdPosts : LiveData<List<PostWithLinksAndComments?>> = postDatabaseDao.getByChildId(currentUserId)
    val posts: LiveData<List<PostWithLinksAndComments?>>
    get(){
        return if (_currentUser.value?.user?.userType == UserType.JONGERE){
            _createdPosts
        } else {
            _managedPosts
        }

    }


    init{
        val app = application as FaithApplication
        val userId : String = app.userProfile?.getId()!!
    }



    fun onCreatePost(post: Post, links: List<Link>){
        uiScope.launch {

             insert(post, links)
        }
    }

    private suspend fun insert(post: Post, links: List<Link>){
        withContext(Dispatchers.IO){
            var postId = postDatabaseDao.insertPost(post)

            links.forEach{
                postDatabaseDao.insertLink(Link(postId = postId, linkString = it.linkString))
            }

        }
    }

    fun onPostClicked(id: Long){
        _navigateToPostDetail.value = id
    }

    fun onPostDetailNavigated(){
        _navigateToPostDetail.value = null
    }




}