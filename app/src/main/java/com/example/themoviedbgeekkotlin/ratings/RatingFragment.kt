package com.example.themoviedbgeekkotlin.ratings

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
import com.example.themoviedbgeekkotlin.databinding.RatingFragmentBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.moviesdetail.FragmentMoviesDetails
import com.example.themoviedbgeekkotlin.ratings.adapter.MovieRatingAdapter
import com.example.themoviedbgeekkotlin.ratings.viewmodel.RatingViewModel
import com.example.themoviedbgeekkotlin.ratings.viewmodel.RatingViewModelFactory
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity

class RatingFragment : Fragment() {

    private var _binding: RatingFragmentBinding? = null
    private val binding get() = _binding!!

    private var recycler: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private lateinit var searchString: String

    private val viewModel: RatingViewModel by viewModels { RatingViewModelFactory() }

    companion object {

        const val BUNDLE_EXTRA_SEARCH_MOVIE = "SEARCH_MOVIE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RatingFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString(RatingFragment.BUNDLE_EXTRA_SEARCH_MOVIE) != null) {
            searchString = arguments?.getString(RatingFragment.BUNDLE_EXTRA_SEARCH_MOVIE, "")!!
        } else searchString = ""

        recycler = binding.movieRatingRecyclerView
        recycler?.layoutManager = LinearLayoutManager(activity)
        recycler?.adapter = MovieRatingAdapter(moviesclickListener)

        setObservers()

        // 3. После того, как текст изменился - вызываем метод ViewModel
        if (searchString != "" && viewModel.searchMoviesLiveData.value.isNullOrEmpty()) {
            viewModel.onSearchQuery(searchString, "ru", true, "RU")
        }
    }

    private fun setObservers() {

        viewModel.searchMoviesLiveData.observe(viewLifecycleOwner, { movieList ->
            (recycler!!.adapter as MovieRatingAdapter).apply {
                setMovie(movieList)
                notifyDataSetChanged()
            }
            binding.progressBarRating?.visibility = View.INVISIBLE
        })


        // observe status
//        viewModel.state.observe(viewLifecycleOwner, { status ->
//            when (status) {
//                is AppState.Init, is AppState.Success -> {
//                    binding.progressBarHistory?.visibility = View.INVISIBLE
//                }
//                is AppState.Loading -> {
//                    binding.progressBarHistory?.visibility = View.VISIBLE
//                }
//                is AppState.Error -> {
//                    binding.progressBarHistory?.visibility = View.INVISIBLE
//                }
//                AppState.EmptyDataSet -> {
//                    binding.progressBarHistory?.visibility = View.INVISIBLE
//                }
//            }
//        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val moviesclickListener = object : OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            Log.d("Parcel", "move.name = ${movie.title}")
            val bundle = Bundle().also {
                it.putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA, movie)
            }

            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
                it.navigate(R.id.moviesdetailFragment, bundle)
            }
        }

        override fun onItemViewClickNotes(movie: MovieEntity) {
//            val bundle = Bundle().also {
//                it.putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA_NOTES, movie)
//            }
//
//            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
//                it.navigate(R.id.moviesdetailFragment, bundle)
//            }
        }
    }
}