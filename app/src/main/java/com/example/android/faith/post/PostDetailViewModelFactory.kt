package com.example.android.faith.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.database.UserDao
import java.lang.IllegalArgumentException

class PostDetailViewModelFactory (
    private val postKey: Long,
    private val userId : String,
    private val postDao : PostDatabaseDao,
    private val userDao : UserDao
        ) : ViewModelProvider.Factory{
            @Suppress("unchecked_cast")
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)){
                    return PostDetailViewModel(postKey, userId, postDao, userDao) as T
                    }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
}