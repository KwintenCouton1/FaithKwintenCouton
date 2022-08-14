package com.example.android.faith.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.database.PostWithLinks

class PostDetailViewModel(private val postKey: Long = 0L,
val dataSource: PostDatabaseDao): ViewModel() {

    private val post = MediatorLiveData<PostWithLinks>()

            fun getPost() = post

    init {
        post.addSource(dataSource.get(postKey), post::setValue)
    }

    private val _navigateToPostList = MutableLiveData<Boolean?>()

    val navigateToPostList: LiveData<Boolean?>
    get() =_navigateToPostList


    fun doneNavigating(){
_navigateToPostList.value =null
    }

    fun onClose(){
        _navigateToPostList.value = true
    }

}