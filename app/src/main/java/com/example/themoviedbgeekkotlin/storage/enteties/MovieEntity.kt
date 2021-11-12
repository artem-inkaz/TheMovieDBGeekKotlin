package com.example.themoviedbgeekkotlin.storage.enteties

import android.os.Parcelable
import androidx.room.*
import com.example.themoviedbgeekkotlin.model.Group
import com.example.themoviedbgeekkotlin.storage.DbContract
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = DbContract.MovieContract.TABLE_NAME
)
@Parcelize
data class MovieEntity (
    @PrimaryKey
    @ColumnInfo(name = DbContract.MovieContract.COLUMN_NAME_ID)
    val id: Long,
    val title: String,
    val overview: String?,
    val dateRelease: String,
    val poster: String?,
    val backdrop: String?,
    val ratings: Float,
    val adult: Boolean,
    val runtime: Int?,
    val reviews: Int,
    val genres: String,
    val like: Boolean = false,
    val notes: String
) : Parcelable
