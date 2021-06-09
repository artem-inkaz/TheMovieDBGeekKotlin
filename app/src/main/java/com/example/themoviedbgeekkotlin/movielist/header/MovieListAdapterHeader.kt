package com.example.themoviedbgeekkotlin.movielist.header

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

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ACTORS = 1
// попытка сделать вывод шапки, не работает нужно доделать проблема с позицией
// работает вывод без шапки, клик по записи и отображение детализации
class MovieListAdapter(private  val itemViewClickListener: OnItemViewClickListener) : RecyclerView.Adapter<MovieListAdapter.MoviesViewHolder>() {
    private var movieData: List<Movie> = listOf()

    //    fun bindMovie(data: List<Movie>) {
    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MoviesViewHolder {
        return when (viewType){
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                    LayoutInflater.from(
                            parent.context
                    ).inflate(R.layout.type_movie, parent, false)
            )
            VIEW_TYPE_ACTORS -> MovieListViewHolder(
                    LayoutInflater.from(
                            parent.context
                    ).inflate(R.layout.view_holder_movie, parent, false)
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
//        holder.bind(movieData[position])
        when (holder) {
         is MovieListViewHolder -> {holder.bind(getItem(position))
         holder.itemView.setOnClickListener {
             itemViewClickListener.onItemViewClick(movieData[position])
         }
         }
//            is HeaderViewHolder.MovieListViewHolder -> holder.bind(movieData[position])
//           is HeaderViewHolder -> holder.bind(movieData.size,movieData.)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (movieData.size) {
            0 -> VIEW_TYPE_HEADER
//            itemCount - 1 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_ACTORS
        }
    }

    fun getItem(position: Int): Movie = movieData[position]

    override fun getItemCount(): Int = movieData.size+1


abstract class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class HeaderViewHolder(itemView: View) : MoviesViewHolder(itemView)

    inner class MovieListViewHolder(view: View): MoviesViewHolder(view){
        private val title: TextView = view.findViewById(R.id.genres)
        private val like : ImageView = view.findViewById(R.id.like)
        fun bind(movie: Movie)  {
            title.text = movie.title
//            poster.setImageResource(movie.poster)
//            ageRating.text = movie.adult
           like.setImageResource(if (movie.like) R.drawable.ic_like else R.drawable.ic_like_empty)
//            genres.text = movie.genres.name
//            ratingBar.rating = movie.ratings.toFloat()
//            reviews.text = movie.reviews.toString() + " REVIEWS"
//            duration.text = movie.runtime.toString() + " MIN"
//            dateRelease.text = movie.dateRelease

//            root.setOnClickListener{
//                itemViewClickListener.onItemViewClick(movie)
//            }
        }
    }

    private val RecyclerView.ViewHolder.context
        get() = this.itemView.context

}