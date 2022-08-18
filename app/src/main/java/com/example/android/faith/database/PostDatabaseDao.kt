package com.example.android.faith.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLink(link : Link): Long

    @Insert
    fun insertComment(comment: Comment): Long


    @Update
    fun updatePost(post: Post)

    @Update
    fun updateLink(link: Link)

    @Update
    fun updateComment(comment:Comment)

    @Query("DELETE FROM link_table where postId = :key")
    fun deleteLinksOfPost(key : Long)

    @Transaction
    @Query("SELECT * from post_table where postId = :key")
    fun get(key: Long): LiveData<PostWithLinksAndComments?>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getAll(): LiveData<List<PostWithLinksAndComments>>

    @Transaction
    @Query("SELECT * FROM POST_TABLE WHERE userId LIKE :key")
    fun getByChildId(key: String) : LiveData<List<PostWithLinksAndComments?>>

    @Transaction
    @Query("SELECT * from POST_TABLE order by created DESC limit 1")
    fun getLatest(): LiveData<PostWithLinksAndComments?>

    @Query("SELECT * from COMMENT_TABLE where postId = :key")
    fun getCommentsByPost(key : Long): LiveData<List<Comment>>

    @Query("SELECT * from COMMENT_TABLE WHERE reactionToCommentId = :key")
    fun getCommentsByParent(key: Long) : LiveData<List<Comment>>


    @Query("SELECT * FROM POST_TABLE inner JOIN user_table on POST_TABLE.userId = user_table.authId inner join UserCoachCrossRef on user_table.authId = UserCoachCrossRef.userId where coachId like :key ")
    fun getPostsByCoachedUsers(key: String): LiveData<List<PostWithLinksAndComments?>>

}