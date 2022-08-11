package com.example.android.faith.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDatabaseDao {

    @Insert
    fun insert(post: Post)

    @Update
    fun update(post: Post)

    @Query("SELECT * from post_table where postId = :key")
    fun get(key: Long): Post?

    @Query("SELECT * FROM post_table")
    fun getAll(): LiveData<List<Post>>

    @Query("SELECT * FROM POST_TABLE WHERE childId = :key")
    fun getByChildId(key: Long) : Post?

    @Query("SELECT * from POST_TABLE order by created DESC limit 1")
    fun getLatest(): Post?

}