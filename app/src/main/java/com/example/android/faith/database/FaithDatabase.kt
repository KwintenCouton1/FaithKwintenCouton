package com.example.android.faith.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.faith.database.daos.JokeDao
import com.example.android.faith.database.post.Comment
import com.example.android.faith.database.post.Link
import com.example.android.faith.database.post.Post
import com.example.android.faith.database.daos.PostDatabaseDao
import com.example.android.faith.database.daos.UserDao
import com.example.android.faith.database.joke.DatabaseJoke
import com.example.android.faith.database.user.*

@Database(entities = [Post::class, Link::class, Comment::class, User::class, UserFavoriteCrossRef::class, UserCoachCrossRef::class, DatabaseJoke::class], version = 18, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FaithDatabase : RoomDatabase(){
    abstract val postDatabaseDao: PostDatabaseDao
    abstract val userDao : UserDao
    abstract val jokeDao : JokeDao


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
                    ).addCallback(seedDatabaseCallback(context))
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }

        }

        private fun seedDatabaseCallback(context: Context):Callback{
            return object : Callback (){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {


                    var dao = getInstance(context).userDao
                    //insert here
                    val begeleider = User(authId = "auth0|62fa477aa41360babda42330", userName = "begeleider", userType = UserType.BEGELEIDER)
                    val jongere1 = User(authId = "auth0|62fb8de6b519e54e03939437", userName = "jongere 1", userType = UserType.JONGERE)
                    val jongere2 = User(authId = "auth0|62fd3cacddb4da0c3af211ac", userName = "jongere 2", userType = UserType.JONGERE)
                    dao.insertUser(begeleider)
                    dao.insertUser(jongere1)
                    dao.insertUser(jongere2)

                    dao.insert(UserCoachCrossRef(userId = jongere1.authId, coachId = begeleider.authId))
                    }

                }

                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    super.onDestructiveMigration(db)


                    ioThread {


                        var dao = getInstance(context).userDao
                        //insert here
                        val begeleider = User(authId = "auth0|62fa477aa41360babda42330", userName = "begeleider", userType = UserType.BEGELEIDER)
                        val jongere1 = User(authId = "auth0|62fb8de6b519e54e03939437", userName = "jongere 1", userType = UserType.JONGERE)
                        val jongere2 = User(authId = "auth0|62fd3cacddb4da0c3af211ac", userName = "jongere 2", userType = UserType.JONGERE)
                        dao.insertUser(begeleider)
                        dao.insertUser(jongere1)
                        dao.insertUser(jongere2)

                        dao.insert(UserCoachCrossRef(userId = jongere1.authId, coachId = begeleider.authId))
                    }
                }
            }
        }
    }
}