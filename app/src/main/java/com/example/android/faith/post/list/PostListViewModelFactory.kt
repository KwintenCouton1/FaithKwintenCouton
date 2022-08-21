package com.example.android.faith.post.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


import com.example.android.faith.database.daos.PostDatabaseDao
import com.example.android.faith.database.daos.UserDao
import java.lang.IllegalArgumentException

class PostListViewModelFactory (
    private val dataSource : PostDatabaseDao,
    private val userDao : UserDao,
    private val currentUserId : String,
    //private val postKey : Long,
    private val application: Application
        ): ViewModelProvider.Factory{
            @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PostListViewModel::class.java)){
                    return PostListViewModel(dataSource, userDao, currentUserId, /*postKey,*/ application) as T
                }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}