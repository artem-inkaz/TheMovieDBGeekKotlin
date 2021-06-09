package com.example.androidacademy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieGroup
import com.example.themoviedbgeekkotlin.movielist.sectionrecyclerviewv2.MoviesAdapter

class MoviesCategoriesAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    private val adapters: List<MoviesAdapter> = listOf()

    private var movieData = listOf<Movie>()
    private var movieDataV2 = listOf<MovieGroup>()

//    fun addItems(movieGroupList: ArrayList<MovieGroup>) = movieDataV2.addAll(movieGroupList)

    fun setMovie(data: List<MovieGroup>) {
        movieDataV2 = data
        notifyDataSetChanged()
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val rootView =
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.view_holder_item_content_container_recycler,
//                parent,
//                false)
//        return CategoryViewHolder(rootView)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_item_content_container_recycler,
                parent,
                false
            ).also {
                Log.d("movieLog", "moviesAdapterCreate")
            }
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = movieDataV2[position]

        (holder as? CategoryViewHolder)?.apply {
            title.text = item.group
//            moviesList.adapter = adapters[position]
            moviesList.setRecycledViewPool(pool)
//                val lm =
//                    LinearLayoutManager(moviesList.context, LinearLayoutManager.HORIZONTAL, false)
//                layoutManager = lm
//
//                val ad = MoviesAdapter()
//                adapter = ad
        }
    }

    override fun getItemCount() = movieDataV2.size

    class CategoryViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.messageTextView)
        val moviesList: RecyclerView = itemView.findViewById(R.id.recyclerViewItem)
    }
}

