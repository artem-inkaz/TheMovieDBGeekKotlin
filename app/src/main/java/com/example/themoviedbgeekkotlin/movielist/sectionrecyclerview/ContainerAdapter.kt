package com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.APP_ACTIVITY
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.moviesdetail.service.DetailsFragment.Companion.BUNDLE_EXTRA

class ContainerAdapter(
    private val context: Context,
    private val sections : List<RecyclerViewSection>,
//    private var moviesclickListener: OnRecyclerMovieClickListener
    ) : RecyclerView.Adapter<ContainerAdapter.ViewHolder>() {

    private var movieData= listOf<Movie>()

    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun create(parent: ViewGroup) : ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_item_content_container_recycler, parent, false)
                return ViewHolder(
                    view
                )
            }
        }

        fun bind(context : Context,section : RecyclerViewSection,movie: List<Movie>) {
            val sectionName = view.findViewById(R.id.messageTextView) as TextView
            val recyclerView = view.findViewById(R.id.recyclerViewItem) as RecyclerView

            sectionName.text = section.label
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false

//            val adapter =
//                ItemRecyclerviewAdapter(
//                    section.items
//                )

            // передача данных во фрагмент детали о фильме
            val adapter = ItemRecyclerviewAdapter(section.items, object  : OnItemViewClickListener {
                override fun onItemViewClick(movie: Movie) {
                    val bundle = Bundle().apply {
                        putParcelable(BUNDLE_EXTRA, movie)
                    }
                   APP_ACTIVITY.navController.navigate(R.id.action_movielistFragment_to_moviesdetailFragment,bundle)
                }

            }).apply {
                setMovie(movie)
            }
//
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
//        val movieData=listOf<Movie>()
        holder.bind(context,section,movieData)
//        holder.itemView.setOnClickListener {
//            moviesclickListener.onClick()
//        }
    }

    override fun getItemCount() = sections.size
}

//interface OnRecyclerMovieClickListener {
//    fun onClick(movie: Movie)
//}