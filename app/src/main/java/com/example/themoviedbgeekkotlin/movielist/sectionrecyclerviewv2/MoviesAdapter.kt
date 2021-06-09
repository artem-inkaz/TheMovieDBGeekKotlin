package com.example.themoviedbgeekkotlin.movielist.sectionrecyclerviewv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.ViewHolderMovieBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie

class MoviesAdapter(
    private var moviesclickListener: OnItemViewClickListener
) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder?>() {

    //    private val movies = listOf<Movie>()
    private var movies: ArrayList<Movie> = ArrayList()
    private lateinit var binding: ViewHolderMovieBinding

    fun clear() = movies.clear()
    fun addItems(moviesList: List<Movie>) = movies.addAll(moviesList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
//        val rootView =LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
        binding = ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MovieViewHolder(rootView)
        return MovieViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_combined_shape)
            .fallback(R.drawable.ic_combined_shape)
            .centerCrop()

        val item = movies[position]

        holder.itemView.setOnClickListener {
            moviesclickListener.onItemViewClick(movies[position])
        }

//        with(holder)= with(binding) {
        with(binding) {
            title.text = item.title

            Glide.with(poster)
                .load(item.poster)
                .apply(imageOption)
                .into(poster)
//            ageRating.text = movie.adult
            like.setImageResource(if (item.like) R.drawable.ic_like else R.drawable.ic_like_empty)
            genres.text = item.genres.joinToString(",")
            ratingBar.rating = item.ratings.toFloat()
            reviews.text = item.reviews.toString() + " REVIEWS"
            duration.text = item.runtime.toString() + " MIN"
            dateRelease.text = item.dateRelease
        }
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.title)
        val poster: ImageView = itemView.findViewById(R.id.poster)
    }
}