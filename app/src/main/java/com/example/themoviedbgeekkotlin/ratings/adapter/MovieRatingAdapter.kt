package com.example.themoviedbgeekkotlin.ratings.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.ListItemHistoryMovieBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

// Для вывода просто списком
class MovieRatingAdapter(
    private val itemViewClickListener: OnItemViewClickListener
) :
    RecyclerView.Adapter<MovieRatingAdapter.MovieListViewHolder>() {

    private var movieData: List<Movie> = listOf()

    //    private lateinit var binding: ViewHolderMovieBinding
    private lateinit var binding: ListItemHistoryMovieBinding

    //    fun bindMovie(data: List<Movie>) {
    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieListViewHolder {
        binding =
            ListItemHistoryMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movieData[position])
        holder.itemView.setOnClickListener {
            itemViewClickListener.onItemViewClick(movieData[position])

        }
    }

    override fun getItemCount(): Int {
        return movieData.size
    }

    inner class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //        companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_combined_shape)
            .fallback(R.drawable.ic_combined_shape)
            .centerCrop()
//        }

        @SuppressLint("SetTextI18n")
       fun bind(movie: Movie) = with(binding) {
            title.text = movie.title
//          poster.setImageResource(movie.poster)
            Glide.with(itemView.context)
                .load(movie.poster)
                .apply(imageOption)
                .into(poster)
            subtitle.text = movie.genres.joinToString(", ")
            rating.text = movie.ratings.toString()

            root.setOnClickListener {
//                itemViewClickListener.onItemViewClick(movie)
            }
        }
    }
}
