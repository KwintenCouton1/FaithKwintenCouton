package com.example.android.faith.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDatabaseDao {

    @Insert
    fun insertPost(post: Post) : Long

    @Insert
    fun insertLink(link : Link): Long

    @Insert
    fun insertComment(comment: Comment): Long

    @Update
    fun updatePost(post: Post)

    @Update
    fun updateLink(link: Link)

    @Update
    fun updateComment(comment:Comment)

    @Transaction
    @Query("SELECT * from post_table where postId = :key")
    fun get(key: Long): LiveData<PostWithLinksAndComments?>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getAll(): LiveData<List<PostWithLinksAndComments>>

    @Transaction
    @Query("SELECT * FROM POST_TABLE WHERE childId = :key")
    fun getByChildId(key: Long) : LiveData<PostWithLinksAndComments?>

    @Transaction
    @Query("SELECT * from POST_TABLE order by created DESC limit 1")
    fun getLatest(): LiveData<PostWithLinksAndComments?>

    @Query("SELECT * from COMMENT_TABLE where postId = :key")
    fun getCommentsByPost(key : Long): LiveData<List<Comment>>

    @Query("SELECT * from COMMENT_TABLE WHERE reactionToCommentId = :key")
    fun getCommentsByParent(key: Long) : LiveData<List<Comment>>

}