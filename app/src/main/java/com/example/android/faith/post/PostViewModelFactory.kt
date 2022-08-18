package com.example.android.faith.post

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.database.UserDao
import java.lang.IllegalArgumentException

class PostViewModelFactory (
    private val dataSource : PostDatabaseDao,
    private val userDao : UserDao,
    private val currentUserId : String,
    private val application: Application
        ): ViewModelProvider.Factory{
            @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PostViewModel::class.java)){
                    return PostViewModel(dataSource, userDao, currentUserId,  application) as T
                }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}