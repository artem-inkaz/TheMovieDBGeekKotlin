package com.example.themoviedbgeekkotlin.model

import android.os.Parcelable
import com.example.themoviedbgeekkotlin.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
        val id: Int = 1,
        val title: String = "Мортал Комбат",
        val genres: Genre = getDefaultGenre(),
        val poster: Int = 0,
        val backdrop: Int = 0,
        val ratings: Double = 5.0,
        val adult: String = "",
        val runtime: Int? = 123,
        val reviews: Int = 500,
        val like: Boolean = false,
        val story: String = "",
        val dateRelease: String = "2020-05-21",
        val actors: Actor = getDefaultActor(),
) : Parcelable

fun getDefaultActor()= Actor("Robert", "Downey Jr.", R.drawable.movie_downey)

fun getDefaultGenre() = Genre(0, "Боевик")
