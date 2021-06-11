package com.example.themoviedbgeekkotlin.storage

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.themoviedbgeekkotlin.App
import com.example.themoviedbgeekkotlin.storage.enteties.ActorEntity
import com.example.themoviedbgeekkotlin.storage.enteties.GroupEntity
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

@Database(entities = [MovieEntity::class, ActorEntity::class,  GroupEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun actorsDao(): ActorsDao
//    abstract fun moviesGroupDao(): MoviesGroupDao
    abstract fun groupDao(): GroupDao

    companion object {
        val instance: MoviesDatabase by lazy {
            Room.databaseBuilder(
                App.context(),
                MoviesDatabase::class.java,
                DbContract.DATABASE_NAME
            ).build()
        }
    }
}