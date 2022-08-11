package com.example.android.faith.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class FaithDatabase : RoomDatabase(){
    abstract val postDatabaseDao: PostDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE : FaithDatabase? = null

        fun getInstance(context: Context): FaithDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FaithDatabase::class.java,
                        "faith_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }

        }
    }
}