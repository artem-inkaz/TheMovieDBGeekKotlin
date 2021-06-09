package com.example.androidacademy.data

import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.model.Actor
import com.example.themoviedbgeekkotlin.model.Genre
import com.example.themoviedbgeekkotlin.model.Movie

class Database_movies {
    fun getMovies() = listOf(
            Movie(
                    1,
                    "Avengers: End Game",
                    Genre(1, "Action, Adventure, Drama"),
                    R.drawable.movie_avengers_end_game,
                    R.drawable.details_movie_avenger_end_game,
                    4.0,
                    "13+",
                    125,
                    500,
                    false,
                    "After the devastating events " +
                            "of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, " +
                            "the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                    "2015-05-25 ",
                    Actor("Robert", "Downey Jr.", R.drawable.movie_downey),

                    ),
            Movie(2,
                    "Tenet",
                    Genre(2, "Action, Sci-Fi, Thriller"),
                    R.drawable.movie_tenet,
                    R.drawable.details_movie_avenger_end_game,
                    5.0,
                    "13+",
                    98,
                    100,
                    true,
                    "After the devastating events " +
                            "of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, " +
                            "the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                    "2018-02-15",
                    Actor("Chris", "Evans", R.drawable.movie_evans)

            ),
            Movie(3,
                    "Black Widow",
                    Genre(3, "Action, Adventure, Fantasy"),
                    R.drawable.movie_black_widow,
                    R.drawable.details_movie_avenger_end_game,
                    4.5,
                    "17+",
                    38,
                    102,
                    false,
                    "After the devastating events " +
                            "of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, " +
                            "the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                    "2017-01-18",
                    Actor("Mark", "Ruffalo", R.drawable.movie_ruffalo)

            ),
            Movie(
                    4,
                    "Wonder Woman 1984",
                    Genre(4, "Action, Adventure, Sci-Fi"),
                    R.drawable.movie_wonder_woman_1984,
                    R.drawable.details_movie_avenger_end_game,
                    3.2,
                    "13+",
                    150,
                    74,
                    false,
                    "After the devastating events " +
                            "of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, " +
                            "the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                    "2018-04-05",
                    Actor("Chris", "Hemsworth", R.drawable.movie_hemsworth)

            ),
    )
}

fun getMoviesNowPlaying(): List<Movie> {
    return listOf(
            Movie(2,
                    "Tenet",
                    Genre(2, "Action, Sci-Fi, Thriller"),
                    R.drawable.movie_tenet,
                    R.drawable.details_movie_avenger_end_game,
                    5.0,
                    "13+",
                    98,
                    100,
                    true,
                    "After the devastating events " +
                            "of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, " +
                            "the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                    "2018-02-15",
                    Actor("Chris", "Evans", R.drawable.movie_evans)

            ),
            Movie(
                    4,
                    "Wonder Woman 1984",
                    Genre(4, "Action, Adventure, Sci-Fi"),
                    R.drawable.movie_wonder_woman_1984,
                    R.drawable.details_movie_avenger_end_game,
                    3.2,
                    "13+",
                    150,
                    74,
                    false,
                    "After the devastating events " +
                            "of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, " +
                            "the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
                    "2018-04-05",
                    Actor("Chris", "Hemsworth", R.drawable.movie_hemsworth)

            ),
    )
}