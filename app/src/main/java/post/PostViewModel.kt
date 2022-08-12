package post

import android.app.Application
import android.graphics.DiscretePathEffect
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.Post
import com.example.android.faith.database.PostDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

public class PostViewModel(
    val database: PostDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val _post = MutableLiveData<Post>()


    val posts : LiveData<List<Post>>
    get() = _posts


    val post : LiveData<Post>
    get() = _post

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope =  CoroutineScope (Dispatchers.Main + viewModelJob)

    private val newPost = MutableLiveData<Post>()

    private val _posts = database.getAll()

    init{
        initializeNewPost()
    }

    private fun initializeNewPost(){
        uiScope.launch {

        }
    }

    private suspend fun getLatestPostFromDatabase(): Post? {
        return withContext(Dispatchers.IO){
            var post = database.getLatest()
            post
        }
    }

    fun onCreatePost(post: Post){
        uiScope.launch {

             insert(post)

            newPost.value = getLatestPostFromDatabase()
        }
        Timber.i(post.text)
    }

    private suspend fun insert(post: Post){
        withContext(Dispatchers.IO){
            database.insert(post)
        }
    }


}