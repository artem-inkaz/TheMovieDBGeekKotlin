package com.example.themoviedbgeekkotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
        val id: Int,
        val name: String,
//        val last_name: String = "",
        val photo_image: String?
) : Parcelable
{
//    val fullName: String
//        get() = "$name $last_name"
}

