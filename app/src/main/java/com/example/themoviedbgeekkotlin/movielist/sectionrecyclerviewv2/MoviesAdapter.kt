package com.example.themoviedbgeekkotlin.movielist.sectionrecyclerviewv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieGroup

class MoviesAdapter :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder?>() {

//    private val movies = listOf<Movie>()
    private var movies: List<Movie> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
        return MovieViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val item = movies[position]

        with(holder) {
            title.text = item.title
//            image.setImageResource(item.imageResource)
        }
    }

    override fun getItemCount(): Int = movies.size

  inner class MovieViewHolder(
      itemView: View
  ) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
//        val image: ImageView = itemView.findViewById(R.id.image)
    }
}