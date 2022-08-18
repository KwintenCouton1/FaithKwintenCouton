package com.example.android.faith.post

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.database.UserDao
import java.lang.IllegalArgumentException

class CreatePostViewModelFactory (
    private val postKey: Long,
    private val postDao : PostDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePostViewModel::class.java)){
            return CreatePostViewModel(postDao,postKey, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}