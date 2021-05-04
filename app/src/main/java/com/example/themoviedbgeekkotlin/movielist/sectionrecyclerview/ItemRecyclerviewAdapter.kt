package com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.ViewHolderMovieBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie

class ItemRecyclerviewAdapter (private val items : List<String>,private  val itemViewClickListener: OnItemViewClickListener) : RecyclerView.Adapter<ItemRecyclerviewAdapter.ViewHolder>() {

    private var movieData: List<Movie> = listOf()

    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        companion object {
            private lateinit var binding: ViewHolderMovieBinding
            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
                binding = ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                return ViewHolder(
//                        view
//                )
                return ViewHolder(binding.root)
            }
        }

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

            root.setOnClickListener{
//               itemViewClickListener.onItemViewClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(
                parent
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
        holder.bind(movieData[position])
        holder.itemView.setOnClickListener {
            itemViewClickListener.onItemViewClick(movieData[position])
        }
    }

    override fun getItemCount() = movieData.size

}