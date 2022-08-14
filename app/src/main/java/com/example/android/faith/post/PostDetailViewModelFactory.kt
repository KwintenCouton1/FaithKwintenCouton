package com.example.android.faith.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.PostDatabaseDao
import java.lang.IllegalArgumentException

class PostDetailViewModelFactory (
    private val postKey: Long,
    private val dataSource : PostDatabaseDao
        ) : ViewModelProvider.Factory{
            @Suppress("unchecked_cast")
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)){
                    return PostDetailViewModel(postKey, dataSource) as T
                    }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
}