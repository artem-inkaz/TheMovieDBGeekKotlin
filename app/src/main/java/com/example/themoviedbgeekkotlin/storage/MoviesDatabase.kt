package com.example.themoviedbgeekkotlin.storage

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.themoviedbgeekkotlin.App
import com.example.themoviedbgeekkotlin.storage.enteties.ActorEntity
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity


@Database(
        entities = [MovieEntity::class, ActorEntity::class],
        version = 1,
        exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun actorsDao(): ActorsDao

    /**
     * https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
    */
// Нужно увеличить version = 1, на version = 2
//    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            // Поскольку мы не изменяли таблицу, здесь больше нечего делать.
//        }
    //Способ 2
    //    database.execSQL («ALTER TABLE users»
    //    + «ADD COLUMN last_update INTEGER»);
//    }
    companion object {
        val instance: MoviesDatabase by lazy {
            Room.databaseBuilder(
                    App.context(),
                    MoviesDatabase::class.java,
                    DbContract.DATABASE_NAME
            )
//            .addMigrations ( MIGRATION_1_2 )
            //  Если не хотим выполнять миграцию и специально хотим, чтобы БД была очищена при обновлении версии
//            . fallbackToDestructiveMigration ()
            .build()
        }
    }
}