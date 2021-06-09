package com.example.themoviedbgeekkotlin.moviesdetail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviedbgeekkotlin.APP_ACTIVITY
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMoviesDetailsFragmentBinding
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.AppState
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieListViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentMoviesDetails : Fragment() {

    private var _binding: FragmentMoviesDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FragmentMovieListViewModel by lazy {
        ViewModelProvider(this).get(FragmentMovieListViewModel::class.java)
    }

    private var adapter: ActorAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesDetailsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable<Movie>(BUNDLE_EXTRA)
        binding.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
        movie?.let {
            with(binding) {
                tvTitle.text = it.title
                imgTitlePoster.setImageResource(movie.backdrop)
                ratingBar.rating = it.ratings.toFloat()
                tvAgeRating.text = it.adult
                tvGenres.text = it.genres.name
                tvReviews.text = it.reviews.toString() + " REVIEWS"
                tvStorylineText.text = it.story
            }
        }

        binding.toolbar.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_moviesdetailFragment_to_movielistFragment)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromLocalStorage()

    }

    // Статус загрузки
    private fun renderData(appState: AppState) {

        when (appState) {
            is AppState.Success -> {
                adapter = ActorAdapter()
                        .apply { setActor(appState.movie) }
                binding.recyclerView.adapter = adapter
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
                Snackbar
                        .make(binding.root, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.reload)) { viewModel.getMovieFromLocalStorage() }
                        .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "movie"

        fun newInstance(bundle: Bundle): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            fragment.arguments = bundle
            return fragment
        }
    }
}