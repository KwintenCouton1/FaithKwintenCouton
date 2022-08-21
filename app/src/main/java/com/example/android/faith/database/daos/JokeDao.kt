package com.example.android.faith.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.faith.database.joke.DatabaseJoke

@Dao
interface JokeDao {
    @Query("select * from databasejoke order by RANDOM() LIMIT 1")
    fun getJokes() : LiveData<DatabaseJoke>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(vararg joke : DatabaseJoke)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg jokes: DatabaseJoke)
}