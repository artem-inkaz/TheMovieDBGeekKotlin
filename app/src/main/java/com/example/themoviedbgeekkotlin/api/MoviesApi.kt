package com.example.themoviedbgeekkotlin.api

import com.example.themoviedbgeekkotlin.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Movies api of themoviedb.org
 */
interface MoviesApi {

  @GET("genre/movie/list")
  suspend fun getGenres(@Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY): GenresDto

  /**
   *  example: https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=<<api_key>>
   */
  @GET("movie/{movie_id}/credits")
  suspend fun getActors(
          @Path("movie_id") movieId: Int,
          @Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY
  ): ActorsDto

  /**
   *  example: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&page=1
   */
  @GET("movie/popular")
  suspend fun getMovies(
          @Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY,
          @Query("page") page: Int = 1
  ): MoviesDto

  /**
   *  example: https://api.themoviedb.org/3/movie/latest?api_key=<<api_key>>&language=en-US
   */
  @GET("movie/latest")
  suspend fun getLatestMovies(
          @Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY
//          @Query("language") language: String
  ): MoviesDto

  /**
   *  example: https://api.themoviedb.org/3/movie/now_playing?api_key=<<api_key>>&language=en-US&page=1
   */
  @GET("movie/now_playing")
  suspend fun getNowPlaying(
          @Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY,
          @Query("page") page: Int = 1
//          @Query("language") language: String
  ): MoviesDto

  /**
   *  example: https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1
   */
  @GET("movie/top_rated")
  suspend fun getTopRated(
          @Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY,
          @Query("page") page: Int = 1
//          @Query("language") language: String

  ): MoviesDto

  /**
   *  example: https://api.themoviedb.org/3/movie/upcoming?api_key=<<api_key>>&language=en-US&page=1
   */
  @GET("movie/upcoming")
  suspend fun getUpComming(
          @Query("api_key") key: String = BuildConfig.THEMOVIEDB_API_KEY,
          @Query("page") page: Int = 1
//          @Query("language") language: String
  ): MoviesDto

}