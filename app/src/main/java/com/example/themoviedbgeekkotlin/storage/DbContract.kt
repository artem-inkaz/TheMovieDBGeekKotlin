package com.example.themoviedbgeekkotlin.storage

import android.provider.BaseColumns

object DbContract {
    const val DATABASE_NAME = "Movies_db"

    /*
     * ПРИМЕЧАНИЕ. В этом случае между фильмом и актерами не будет таблицы «многие ко многим».
     * Информация о том, в каком фильме принимает участие актер - хранится в таблице актеров
    * */

    object MovieContract {
        const val TABLE_NAME = "movie"

        const val COLUMN_NAME_ID = BaseColumns._ID

        const val COLUMN_NAME_MOVIE_GROUP_ID = "group_id"
    }

    object ActorContract {
        const val TABLE_NAME = "actor"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_ACTOR_ID = "actor_id"
        const val COLUMN_NAME_IMAGE = "image_url"
        const val COLUMN_NAME_MOVIE_ID = "movie_id"
    }

}
