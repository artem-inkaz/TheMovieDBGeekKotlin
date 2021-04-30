package com.example.themoviedbgeekkotlin.movielist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMovieListFragmentBinding
import com.example.themoviedbgeekkotlin.databinding.ViewHolderMovieBinding
import com.example.themoviedbgeekkotlin.model.Movie
import com.google.android.material.snackbar.Snackbar

class FragmentMovieList : Fragment() {

    private var _binding: ViewHolderMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FragmentMovieListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ViewHolderMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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
                val movieData = appState.movie
                binding.loadingLayout.visibility = View.GONE
                setData(movieData)
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
        binding.title.text = movieData.title
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentMovieList()
    }
}