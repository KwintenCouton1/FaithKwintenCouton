package com.example.android.faith

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.faith.database.daos.UserDao
import java.lang.IllegalArgumentException

class AccountViewModelFactory(
    private val userId : String = "",
    private val dataSource : UserDao,
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)){
            return AccountViewModel(userId, dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}