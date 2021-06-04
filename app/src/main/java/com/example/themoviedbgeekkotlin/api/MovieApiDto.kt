package com.example.themoviedbgeekkotlin.api

import com.google.gson.annotations.SerializedName

// movies
data class MoviesDto(
    val results: List<MovieDto>

)

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("backdrop_path")
    val backdrop: String?,
    @SerializedName("vote_average")
    val ratings: Float,
    val adult: Boolean,
    val runtime: Int? = null,
    @SerializedName("vote_count")
    val reviews: Int,
    @SerializedName("genre_ids")
    val genreIds: List<Int>
)

data class MovieDtoEx(
        val id: Int,
        val title: String?,
        val overview: String?,
        @SerializedName("backdrop_path")
        val backdrop: String?,
        @SerializedName("vote_average")
        val ratings: Float,
        val runtime: Int? = null,
        @SerializedName("vote_count")
        val reviews: Int
)

// genres
data class GenresDto(
    val genres: List<GenreDto>
)


data class GenreDto(
    val id: Int,
    val name: String
)

// actors

data class ActorsDto(
    @SerializedName("cast")
    val actors: List<ActorDto>
)


data class ActorDto(
    val id: Int,
    val name: String,
    @SerializedName("profile_path")
    val image: String? = null
)

