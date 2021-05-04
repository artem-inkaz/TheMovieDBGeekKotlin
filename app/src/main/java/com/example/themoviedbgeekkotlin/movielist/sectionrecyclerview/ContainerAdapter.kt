package com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademy.data.Database_movies
import com.example.themoviedbgeekkotlin.APP_ACTIVITY
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.moviesdetail.FragmentMoviesDetails

class ContainerAdapter(private val context: Context, private val sections : List<RecyclerViewSection>) : RecyclerView.Adapter<ContainerAdapter.ViewHolder>() {

    class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun create(parent: ViewGroup) : ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_item_content_container_recycler, parent, false)
                return ViewHolder(
                    view
                )
            }
        }

        fun bind(section : RecyclerViewSection,movie: List<Movie>) {
            val sectionName = view.findViewById(R.id.messageTextView) as TextView
            val recyclerView = view.findViewById(R.id.recyclerViewItem) as RecyclerView

            sectionName.text = section.label
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false

//            val adapter =
//                ItemRecyclerviewAdapter(
//                    section.items
//                ).apply { setMovie(Database_movies().getMovies()) }

            val adapter = ItemRecyclerviewAdapter(section.items, object  : OnItemViewClickListener {
                    override fun onItemViewClick(movie: Movie) {
                        val bundle = Bundle().apply {
                            putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA, movie)
                        }
                        APP_ACTIVITY.navController.navigate(R.id.action_movielistFragment_to_moviesdetailFragment,bundle)
                    }

                }).apply {
                    setMovie(Database_movies().getMovies())
                }

            recyclerView.adapter = adapter

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
        val movieData: List<Movie> = listOf()
        holder.bind(section,movieData)
    }

    override fun getItemCount() = sections.size


}