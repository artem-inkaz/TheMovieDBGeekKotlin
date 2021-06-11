package com.example.themoviedbgeekkotlin.moviesdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.ViewHolderActorBinding
import com.example.themoviedbgeekkotlin.model.Actor

class ActorAdapter : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    private var actorsList = listOf<Actor>()
    private lateinit var binding: ViewHolderActorBinding

    fun setActor(data: List<Actor>) {
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

        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_combined_shape)
            .fallback(R.drawable.ic_combined_shape)
            .centerCrop()

        fun bind(actor: Actor) = with(binding) {
            Glide.with(itemView.context)
                .load(actor.photo_image)
                .apply(imageOption)
                .into(ivActorsImage)

            tvActorFullName?.text = actor.name
        }
    }
}

