package com.example.themoviedbgeekkotlin.movielist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.ViewHolderMovieBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie

// Для вывода просто списком
class MovieListAdapter(private  val itemViewClickListener: OnItemViewClickListener) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {
    private var movieData: List<Movie> = listOf()
    private lateinit var binding: ViewHolderMovieBinding

    //    fun bindMovie(data: List<Movie>) {
    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MovieListViewHolder {
        binding = ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount(): Int {
        return movieData.size
    }

    inner class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) = with(binding) {
            title.text = movie.title
            poster.setImageResource(movie.poster)
            ageRating.text = movie.adult
            like.setImageResource(if (movie.like) R.drawable.ic_like else R.drawable.ic_like_empty)
            genres.text = movie.genres.name
            ratingBar.rating = movie.ratings.toFloat()
            reviews.text = movie.reviews.toString() + " REVIEWS"
            duration.text = movie.runtime.toString() + " MIN"
            dateRelease.text = movie.dateRelease

            root.setOnClickListener {
                itemViewClickListener.onItemViewClick(movie)
            }
        }
    }
}