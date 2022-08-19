package com.example.android.faith.post.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.faith.FaithApplication
import com.example.android.faith.database.*
import kotlinx.coroutines.*

public class PostListViewModel(
    val postDatabaseDao: PostDatabaseDao,
    val userDao : UserDao,
    val currentUserId : String,
    //val postKey : Long,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()


    private val _navigateToPostDetail = MutableLiveData<Long?>()
    val navigateToPostDetail
    get() = _navigateToPostDetail

    private val _showFavorites : MutableLiveData<Boolean> = MutableLiveData(false)
    val showFavorites: LiveData<Boolean>
    get() = _showFavorites


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

    private var _favoritedPosts: LiveData<List<PostWithLinksAndComments?>> = postDatabaseDao.getFavoritedPosts(currentUserId)
    val posts: LiveData<List<PostWithLinksAndComments?>>
    get(){
        return if (_currentUser.value?.user?.userType == UserType.JONGERE){
            if (_showFavorites.value!!) {
                _favoritedPosts
            }
            _createdPosts
        } else {
            _managedPosts
        }

    }


    init{
        val app = application as FaithApplication
        val userId : String = app.userProfile?.getId()!!
    }

    fun toggleFavorites(value: Boolean){
        _showFavorites.value = value
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