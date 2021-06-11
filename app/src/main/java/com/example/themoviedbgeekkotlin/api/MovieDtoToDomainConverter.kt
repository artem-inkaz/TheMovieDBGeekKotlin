package com.example.themoviedbgeekkotlin.api

import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Конвертер чтобы в результирующем классе Movie были все нужные поля
suspend fun convertMovieDtoToDomain(
    moviesDto: List<MovieDto>,
    genres: List<GenreDto>
): List<Movie> =
    withContext(Dispatchers.Default) {
        val genresMap: Map<Int, GenreDto> = genres.associateBy { it.id }

        moviesDto.map { movieDto: MovieDto ->
            Movie(
                id = movieDto.id,
                title = movieDto.title,
                overview = movieDto?.overview,
                dateRelease = movieDto?.dateRelease,
                poster = movieDto.poster.let { BuildConfig.BASE_IMAGE_URL + movieDto.poster },
                backdrop = movieDto.backdrop.let { BuildConfig.BASE_IMAGE_URL + movieDto.backdrop },
                ratings = movieDto.ratings / 2,
                adult = movieDto.adult,
                runtime = movieDto?.runtime,
                reviews = movieDto.reviews,
                genres = movieDto.genreIds.map {
                    genresMap[it]?.name.toString()
                }
            )
        }
    }


