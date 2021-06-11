package com.example.themoviedbgeekkotlin.storage.enteties

import androidx.room.*
import com.example.themoviedbgeekkotlin.model.Group
import com.example.themoviedbgeekkotlin.storage.DbContract

//@Entity(tableName = DbContract.MovieContract.TABLE_NAME)
//@Entity(
//    tableName = DbContract.MovieContract.TABLE_NAME,
//    foreignKeys = [ForeignKey(
//        entity = MovieGroupEntity::class,
//        parentColumns = arrayOf(DbContract.MovieGroupContract.COLUMN_NAME_ID),
//        childColumns = arrayOf(DbContract.MovieContract.COLUMN_NAME_MOVIE_GROUP_ID),
//        onDelete = ForeignKey.CASCADE
//    )],
//    indices = [Index(value = [DbContract.MovieContract.COLUMN_NAME_MOVIE_GROUP_ID])]
//)
//@Entity(
//    foreignKeys = @ForeignKey(entity = Group.class,
//            parentColumns = "id",
//        childColumns = "group_id",
//        onDelete = ForeignKey.CASCADE),
//    primaryKeys = {"group_id", "id"})



@Entity(
    tableName = DbContract.MovieContract.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = GroupEntity::class,
        parentColumns = arrayOf(DbContract.GroupContract.COLUMN_NAME_ID),
        childColumns = arrayOf(DbContract.MovieContract.COLUMN_NAME_MOVIE_GROUP_ID),
        onDelete = ForeignKey.CASCADE,
    )],
//     primaryKeys = arrayOf(DbContract.MovieContract.COLUMN_NAME_ID,DbContract.MovieContract.COLUMN_NAME_MOVIE_GROUP_ID),
   indices = [Index(value = [DbContract.MovieContract.COLUMN_NAME_MOVIE_GROUP_ID])]
)

data class MovieEntity (
    @PrimaryKey
    @ColumnInfo(name = DbContract.MovieContract.COLUMN_NAME_ID)
    val movieId: Long,
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
    @ColumnInfo(name = DbContract.MovieContract.COLUMN_NAME_MOVIE_GROUP_ID)
    val groupId: Int,
//    @ColumnInfo(name = DbContract.MovieContract.CO)
//    val groupName: String
)
