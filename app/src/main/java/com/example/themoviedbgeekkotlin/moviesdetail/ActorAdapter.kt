package com.example.themoviedbgeekkotlin.moviesdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.databinding.ViewHolderActorBinding
import com.example.themoviedbgeekkotlin.model.Actor
import com.example.themoviedbgeekkotlin.model.Movie

class ActorAdapter : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    private var actorsList = listOf<Movie>()
    private lateinit var binding: ViewHolderActorBinding

    fun setActor(data: List<Movie>) {
        actorsList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ActorViewHolder {
        binding = ViewHolderActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actorsList[position])
    }

    override fun getItemCount(): Int = actorsList.size


    inner class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) = with(binding) {
            ivActorsImage.setImageResource(movie.actors.photo_image)
            tvActorFullName.text = movie.actors.fullName
        }
    }
}

