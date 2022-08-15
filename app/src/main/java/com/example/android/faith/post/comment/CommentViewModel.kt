package com.example.android.faith.post.comment

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.faith.database.Comment
import com.example.android.faith.database.PostDatabaseDao
import kotlinx.coroutines.*

class CommentViewModel(val database: PostDatabaseDao,
application: Application): AndroidViewModel(application) {
    private var viewModelJob = Job()

    private lateinit var  _commentsOfPost: LiveData<List<Comment>>

    fun getCommentsOfPost(key: Long) :  LiveData<List<Comment>>{
        _commentsOfPost = database.getCommentsByPost(key)
        return _commentsOfPost
    }

    private lateinit var _commentsOfParent : LiveData<List<Comment>>

    fun getCommentsOfParent(key : Long) : LiveData<List<Comment>>{

        _commentsOfParent = database.getCommentsByParent(key)
        return _commentsOfParent
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun onSubmitComment(comment: Comment){
        uiScope.launch{
            insert(comment)
        }
    }

    private suspend fun insert(comment: Comment){
        withContext(Dispatchers.IO){
            var commentId = database.insertComment(comment)
        }
    }


}