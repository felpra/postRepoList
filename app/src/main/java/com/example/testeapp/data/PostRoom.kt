package com.example.testeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testeapp.model.PostWithUser
import com.example.testeapp.model.UserConverter

@Database(entities = [PostWithUser::class], version = 1)
@TypeConverters(UserConverter::class)
abstract class PostRoom : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {

        private const val DB_NAME = "post-db"

        // For Singleton instantiation
        @Volatile
        private var instance: PostRoom? = null

        fun getInstance(context: Context): PostRoom {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PostRoom {
            return Room.databaseBuilder(
                context,
                PostRoom::class.java, DB_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }

}