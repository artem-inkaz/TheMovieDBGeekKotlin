package com.example.themoviedbgeekkotlin.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// movies
@Serializable
data class MoviesDto(
    val results: List<MovieDto>
)

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerialName("poster_path")
    val poster: String?,
    @SerialName("backdrop_path")
    val backdrop: String?,
    @SerialName("vote_average")
    val ratings: Float,
    val adult: Boolean,
    val runtime: Int? = null,
    @SerialName("vote_count")
    val reviews: Int,
    @SerialName("genre_ids")
    val genreIds: List<Int>
)

// genres
@Serializable
data class GenresDto(
    val genres: List<GenreDto>
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)

// actors
@Serializable
data class ActorsDto(
    @SerialName("cast")
    val actors: List<ActorDto>
)

@Serializable
data class ActorDto(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val image: String? = null
)

