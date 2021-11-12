package com.example.themoviedbgeekkotlin.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedbgeekkotlin.storage.enteties.ActorEntity

@Dao
interface ActorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<ActorEntity>)

    @Query("DELETE FROM ACTOR WHERE movie_id == :movieId")
    suspend fun deleteByMovieId(movieId: Int)

    @Query("SELECT * FROM ACTOR WHERE movie_id == :movieId ORDER BY _id ASC")
    suspend fun getAllByMovieId(movieId: Int): List<ActorEntity>
}