package com.example.testeapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val login: String,
    val id: Long,
    val avatar_url: String
): Parcelable
