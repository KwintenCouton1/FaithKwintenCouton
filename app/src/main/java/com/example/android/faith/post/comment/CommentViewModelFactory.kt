package com.example.android.faith.post.comment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.faith.database.PostDatabaseDao
import java.lang.IllegalArgumentException

class CommentViewModelFactory (
    private val commentKey : Long = 0L,
    private val dataSource : PostDatabaseDao,
    private val application: Application): ViewModelProvider.Factory
        {
            @Suppress("unchecked_cast")
            override fun <T:ViewModel> create(modelClass: Class<T>):T{
                if (modelClass.isAssignableFrom(CommentViewModel::class.java)){
                    return CommentViewModel(commentKey, dataSource, application) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
}