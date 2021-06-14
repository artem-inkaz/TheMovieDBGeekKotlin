package com.example.themoviedbgeekkotlin.storage

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
     * https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
     * https://medium.com/androiddevelopers/testing-room-migrations-be93cdb0d975
     * https://github.com/android/architecture-components-samples/tree/master/PersistenceMigrationsSample
    */
// Нужно увеличить version = 1, на version = 2
//    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
        // Способ 2
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