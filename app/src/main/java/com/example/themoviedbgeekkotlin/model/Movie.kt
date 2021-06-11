package com.example.themoviedbgeekkotlin.model

import android.os.Parcelable
import com.example.themoviedbgeekkotlin.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
        val id: Int,
        val title: String,
        val overview: String?,
        val dateRelease: String?,
        val poster: String?,
        val backdrop: String?,
        val ratings: Float,
        val adult: Boolean,
        val runtime: Int?,
        val reviews: Int,
        val genres: List<String>,
        val like: Boolean = false
) : Parcelable

//fun getDefaultActor()= Actor("Robert", "Downey Jr.", R.drawable.movie_downey)
//fun getDefaultGenre() = Genre(0, "Боевик")
