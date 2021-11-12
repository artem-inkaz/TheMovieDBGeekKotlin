package com.example.themoviedbgeekkotlin.moviesdetail

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedbgeekkotlin.APP_ACTIVITY
import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.api.MovieDto
import com.example.themoviedbgeekkotlin.databinding.FragmentMoviesDetailsFragmentBinding
import com.example.themoviedbgeekkotlin.model.Actor
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.AppState
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieListViewModel
import com.example.themoviedbgeekkotlin.movielist.MoviesListViewModelFactory
import com.example.themoviedbgeekkotlin.moviesdetail.internet.MoviesLoader
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity
import com.google.android.material.snackbar.Snackbar

class FragmentMoviesDetails : Fragment() {

    private var _binding: FragmentMoviesDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FragmentMoviesDetailsViewModel by viewModels { MoviesDetailViewModelFactory() }
    private val viewModelList: FragmentMovieListViewModel by viewModels { MoviesListViewModelFactory() }

    private var adapter: ActorAdapter? = null

    // Для загрузки из MovieList
    private lateinit var movieBundle: Movie
    // Для загрузки из History
    private lateinit var movieBundleNotes: MovieEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesDetailsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getParcelable<Movie>(BUNDLE_EXTRA_RATING) != null) {
            movieBundle = arguments?.getParcelable<Movie>(BUNDLE_EXTRA_RATING)!!
            movieBundle.let { movie ->
                displayMovie(movie)
            }
        }

        if (arguments?.getParcelable<Movie>(BUNDLE_EXTRA) != null) {
            movieBundle = arguments?.getParcelable<Movie>(BUNDLE_EXTRA)!!
            movieBundle.let { movie ->
                displayMovie(movie)
            }
        }
        if (arguments?.getParcelable<MovieEntity>(BUNDLE_EXTRA_NOTES) != null) {
            movieBundleNotes = arguments?.getParcelable<MovieEntity>(BUNDLE_EXTRA_NOTES)!!
            movieBundleNotes.let { movieNotes ->
                displayMovieNotes(movieNotes)
            }
        }

        adapter = ActorAdapter()
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        binding.toolbar.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
                it.navigate(R.id.action_moviesdetailFragment_to_movielistFragment)
            }
        }

        binding.saveMovie.setOnClickListener {
            viewModelList.saveMoviesLocally(movieBundle, binding.notesMovie.text.toString())
        }

        setObservers()
    }

    private fun setObservers() {
        // observe actors data
        viewModel.actors.observe(viewLifecycleOwner, {
            setActorsData(it)
        })
    }

    private fun setActorsData(actors: List<Actor>) {

        if (actors.isNotEmpty()) {
            (binding.recyclerView?.adapter as ActorAdapter).setActor(actors)
        }
    }

    private fun displayMovie(movie: Movie) {

        with(binding) {
            tvTitle.text = movie.title
            Glide.with(root.context)
                .load(BuildConfig.BASE_IMAGE_URL + movie.backdrop)
                .apply(imageOption)
                .into(imgTitlePoster)
            ratingBar.rating = movie.ratings / 2
            tvReviews.text = movie.reviews.toString() + " REVIEWS"
            tvStorylineText.text = movie.overview
            movie.let {
                viewModel.getActors(it.id)
                viewModel.saveActorsLocally(it.id)
            }
        }
    }

    private fun displayMovieNotes(movie: MovieEntity) {

        with(binding) {
            tvTitle.text = movie.title
            Glide.with(root.context)
                .load(BuildConfig.BASE_IMAGE_URL + movie.backdrop)
                .apply(imageOption)
                .into(imgTitlePoster)
            ratingBar.rating = movie.ratings / 2
            tvReviews.text = movie.reviews.toString() + " REVIEWS"
            tvStorylineText.text = movie.overview
            movie.let {
                viewModel.getActors(it.id.toInt())
                viewModel.saveActorsLocally(it.id.toInt())
            }
            notesMovie.append(movie.notes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "movie"
        const val BUNDLE_EXTRA_NOTES = "movieNotes"
        const val BUNDLE_EXTRA_RATING = "movieRating"

        fun newInstance(bundle: Bundle): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            fragment.arguments = bundle
            return fragment
        }

        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_combined_shape)
            .fallback(R.drawable.ic_combined_shape)
            .centerCrop()
    }
}