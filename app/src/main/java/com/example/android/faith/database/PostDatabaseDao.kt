package com.example.android.faith.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDatabaseDao {

    @Insert
    fun insertPost(post: Post) : Long

    @Insert
    fun insertLink(link : Link)

    @Update
    fun updatePost(post: Post)

    @Update
    fun updateLink(link: Link)

    @Transaction
    @Query("SELECT * from post_table where postId = :key")
    fun get(key: Long): LiveData<PostWithLinks?>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getAll(): LiveData<List<PostWithLinks>>

    @Transaction
    @Query("SELECT * FROM POST_TABLE WHERE childId = :key")
    fun getByChildId(key: Long) : LiveData<PostWithLinks?>

    @Transaction
    @Query("SELECT * from POST_TABLE order by created DESC limit 1")
    fun getLatest(): LiveData<PostWithLinks?>

}