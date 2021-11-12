package com.example.themoviedbgeekkotlin.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentFavouriteBinding
import com.example.themoviedbgeekkotlin.favourite.history.MovieHistoryAdapter
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.AppState
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieListViewModel
import com.example.themoviedbgeekkotlin.movielist.MoviesListViewModelFactory
import com.example.themoviedbgeekkotlin.moviesdetail.FragmentMoviesDetails
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

class FragmentFavourite : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private var recycler: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private val viewModel: FragmentMovieListViewModel by viewModels { MoviesListViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = binding.movieHistoryRecyclerView
        recycler?.layoutManager = LinearLayoutManager(activity)
        recycler?.adapter = MovieHistoryAdapter(moviesclickListener)

        setObservers()
        // без заметок
//        if (viewModel.movie.value.isNullOrEmpty()) {   // to avoid unnecessary request, when we came back from the detail screen
//            viewModel.loadMoviesFromDb()
//        }

        if (viewModel.movieNotes.value.isNullOrEmpty()) {   // to avoid unnecessary request, when we came back from the detail screen
            viewModel.loadMoviesNotesFromDb()
        }
    }

    override fun onStart() {
        super.onStart()
        // без заметок
//        viewModel.loadMoviesFromDb()
        viewModel.loadMoviesNotesFromDb()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        // observe movies data
//        viewModel.movie.observe(viewLifecycleOwner, { movieList ->
//            (recycler!!.adapter as MovieHistoryAdapter).apply {
//                setMovie(movieList)
//            }
//            binding.progressBarHistory?.visibility = View.INVISIBLE
//        })
        viewModel.movieNotes.observe(viewLifecycleOwner, { movieList ->
            (recycler!!.adapter as MovieHistoryAdapter).apply {
                setMovieNotes(movieList)
            }
            binding.progressBarHistory?.visibility = View.INVISIBLE
        })


        // observe status
        viewModel.state.observe(viewLifecycleOwner, { status ->
            when (status) {
                is AppState.Init, is AppState.Success -> {
                    binding.progressBarHistory?.visibility = View.INVISIBLE
                }
                is AppState.Loading -> {
                    binding.progressBarHistory?.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    binding.progressBarHistory?.visibility = View.INVISIBLE
                }
                AppState.EmptyDataSet -> {
                    binding.progressBarHistory?.visibility = View.INVISIBLE
                }
            }
        })
    }

    companion object {
        fun newInstance() = FragmentFavourite()
    }

    private val moviesclickListener = object : OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
//            Log.d("Parcel", "move.name = ${movie.title}")
//        val bundle = Bundle().also {
//            it.putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA, movie)
//        }
//
//        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
//            it.navigate(R.id.moviesdetailFragment, bundle)
//        }
        }

        override fun onItemViewClickNotes(movie: MovieEntity) {
            val bundle = Bundle().also {
                it.putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA_NOTES, movie)
            }

            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
                it.navigate(R.id.moviesdetailFragment, bundle)
            }
        }
    }
}