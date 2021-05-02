package com.example.themoviedbgeekkotlin.movielist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.themoviedbgeekkotlin.APP_ACTIVITY
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMovieListFragmentBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.moviesdetail.FragmentMoviesDetails
import com.google.android.material.snackbar.Snackbar

class FragmentMovieList : Fragment() {

    private var _binding: FragmentMovieListFragmentBinding? = null
    private val binding get() = _binding!!

    private var adapter: MovieListAdapter? =null

    private lateinit var viewModel: FragmentMovieListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.movieListRecyclerView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentMovieListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromLocalStorage()
    }

    // Статус загрузки
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter = MovieListAdapter(object  : OnItemViewClickListener{
                    override fun onItemViewClick(movie: Movie) {
                        val bundle = Bundle().apply {
                            putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA, movie)
                        }
                        APP_ACTIVITY.navController.navigate(R.id.action_movielistFragment_to_moviesdetailFragment,bundle)
                    }

                }).apply {
                    setMovie(appState.movie)
                }
//                setData(movieData)
                binding.movieListRecyclerView.adapter = adapter
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                        .make(binding.loadingLayout, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.reload)) { viewModel.getMovieFromLocalStorage() }
                        .show()
            }
        }
    }

    private fun setData(movieData: Movie) {
//        binding.title.text = movieData.title
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentMovieList()
    }
}