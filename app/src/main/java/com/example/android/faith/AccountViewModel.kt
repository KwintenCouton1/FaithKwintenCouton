package com.example.android.faith

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.database.User
import com.example.android.faith.database.UserDao
import com.example.android.faith.database.UserWithPostsAndLinksAndComments
import kotlinx.coroutines.*

class AccountViewModel(private val userId : String = "",
                       val database: UserDao,
                       application: Application
): AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val user = MediatorLiveData<UserWithPostsAndLinksAndComments>()
    fun getUser() = user

    init{
        user.addSource(database.getUser(userId), user::setValue)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun updateUser(user: User){
        uiScope.launch {
            update(user)
        }
    }

    private suspend fun update(user: User){
        withContext(Dispatchers.IO){
            database.updateUser(user)
        }
    }

}