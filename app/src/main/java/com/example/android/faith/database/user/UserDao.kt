package com.example.android.faith.database.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.faith.database.post.Post


@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)


    @Update
    fun updateUser(user: User)

    @Insert
    fun insert(userCoachCrossRef: UserCoachCrossRef): Long

    @Insert
    fun insertUserFavoriteCrossRef(userFavoriteCrossRef: UserFavoriteCrossRef)


    @Transaction
    @Query("Select * from user_table where authId = :key limit 1")
    fun getUser(key: String) : LiveData<UserWithPostsAndLinksAndComments?>

    @Transaction
    @Query("select * from post_table inner join UserFavoriteCrossRef on post_table.postId = UserFavoriteCrossRef.postId  where UserFavoriteCrossRef.authId like :key")
    fun getUserFavorites(key: String) : LiveData<List<Post>>

    @Transaction
    @Query("SELECT * FROM user_table left join UserCoachCrossRef on user_table.authId = UserCoachCrossRef.userId where coachId like :key")
    fun getUsersManagedBy(key: String) : LiveData<List<UserWithPostsAndLinksAndComments>>

    @Delete
    fun deleteUserFavoriteCrossRef(userFavoriteCrossRef: UserFavoriteCrossRef)


}