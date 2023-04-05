package com.example.testeapp.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: User,
    val forks: Int
): Parcelable

@Parcelize
@Entity(tableName = "posts")
data class PostWithUser(
    @NonNull
    @PrimaryKey
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: User,
    val forks: Int,
    var isFavorite: Boolean = false
): Parcelable

class UserConverter {
    @TypeConverter
    fun fromJson(json: String): User {
        return Gson().fromJson(json, User::class.java)
    }

    @TypeConverter
    fun toJson(user: User): String {
        return Gson().toJson(user)
    }
}
