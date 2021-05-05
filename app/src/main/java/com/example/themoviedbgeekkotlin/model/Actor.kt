package com.example.themoviedbgeekkotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
        val name: String = "",
        val last_name: String = "",
        val photo_image: Int = 0
) : Parcelable
{
    val fullName: String
        get() = "$name $last_name"
}

