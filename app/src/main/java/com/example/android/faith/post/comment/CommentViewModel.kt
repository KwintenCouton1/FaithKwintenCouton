package com.example.android.faith.post.comment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.faith.database.post.Comment
import com.example.android.faith.database.daos.PostDatabaseDao
import kotlinx.coroutines.*

class CommentViewModel(
    private val commentKey : Long = 0L,
    val database: PostDatabaseDao,
    application: Application): AndroidViewModel(application) {
    private var viewModelJob = Job()

    private lateinit var  _commentsOfPost: LiveData<List<Comment>>

    private val _navigateToCommentReactions = MutableLiveData<Long?>()
    val navigateToCommentReactions
        get() = _navigateToCommentReactions

    private val comment = MediatorLiveData<Comment>()
    fun getComment() = comment

    init{
        comment.addSource(database.getComment(commentKey), comment::setValue)
    }

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

    fun onDisplayReactions(commentId: Long) {
        _navigateToCommentReactions.value = commentId
    }

    fun onReactionsDisplayed(){
        _navigateToCommentReactions.value = null
    }

    fun onDeleteComment(comment: Comment) {
        uiScope.launch {
            delete(comment)
        }

    }

    private suspend fun delete(comment: Comment){
        withContext(Dispatchers.IO){
            database.deleteComment(comment)
        }
    }

    fun onUpdateComment(comment: Comment) {
        uiScope.launch {
            update(comment)
        }

    }

    private suspend fun update(comment: Comment){
        withContext(Dispatchers.IO){
            database.updateComment(comment)
        }
    }


}