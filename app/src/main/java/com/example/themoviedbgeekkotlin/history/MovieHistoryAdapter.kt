package com.example.themoviedbgeekkotlin.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.ListItemHistoryMovieBinding
import com.example.themoviedbgeekkotlin.databinding.ViewHolderMovieBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

// Для вывода просто списком
class MovieHistoryAdapter(
    private val itemViewClickListener: OnItemViewClickListener
) :
    RecyclerView.Adapter<MovieHistoryAdapter.MovieListViewHolder>() {

    private var movieData: List<Movie> = listOf()
    private var movieDataNotes: List<MovieEntity> = listOf()

    //    private lateinit var binding: ViewHolderMovieBinding
    private lateinit var binding: ListItemHistoryMovieBinding

    //    fun bindMovie(data: List<Movie>) {
    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    fun setMovieNotes(data: List<MovieEntity>) {
        movieDataNotes = data
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
//        holder.bind(movieData[position])
        holder.bind(movieDataNotes[position])

        holder.itemView.setOnClickListener {
//            itemViewClickListener.onItemViewClick(movieData[position])
            itemViewClickListener.onItemViewClickNotes(movieDataNotes[position])
        }
    }

    override fun getItemCount(): Int {
//        return movieData.size
        return movieDataNotes.size
    }

    inner class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //        companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_combined_shape)
            .fallback(R.drawable.ic_combined_shape)
            .centerCrop()
//        }

        @SuppressLint("SetTextI18n")
//       fun bind(movie: Movie) = with(binding) {
        fun bind(movie: MovieEntity) = with(binding) {
            title.text = movie.title
//          poster.setImageResource(movie.poster)
            Glide.with(itemView.context)
                .load(movie.poster)
                .apply(imageOption)
                .into(poster)
            subtitle.text = movie.genres
            rating.text = movie.ratings.toString()
            notes.text = movie.notes
            root.setOnClickListener {
//                itemViewClickListener.onItemViewClick(movie)
            }
        }
    }
}
