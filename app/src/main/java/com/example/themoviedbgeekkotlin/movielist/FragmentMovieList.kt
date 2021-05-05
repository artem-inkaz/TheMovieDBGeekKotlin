package com.example.themoviedbgeekkotlin.movielist

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
import com.example.themoviedbgeekkotlin.databinding.FragmentMovieListFragmentBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview.ContainerAdapter
import com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview.DataStore
import com.example.themoviedbgeekkotlin.moviesdetail.FragmentMoviesDetails
import com.google.android.material.snackbar.Snackbar

class FragmentMovieList : Fragment() {

    private var _binding: FragmentMovieListFragmentBinding? = null
    private val binding get() = _binding!!

//    private var adapter: MovieListAdapter? =null расскомментировать если будем работать через MovieListAdapter
    private var adapter: ContainerAdapter? =null

    private val viewModel: FragmentMovieListViewModel by lazy {
        ViewModelProvider(this).get(FragmentMovieListViewModel::class.java)
    }

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
        binding.movieListRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromLocalStorage()
    }

    // Статус загрузки
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
//*************** код если указат ь в качестве адаптера ** MovieListAdapter ***
//                adapter = MovieListAdapter(object  : OnItemViewClickListener{
//                    override fun onItemViewClick(movie: Movie) {
//                        val bundle = Bundle().apply {
//                            putParcelable(FragmentMoviesDetails.BUNDLE_EXTRA, movie)
//                        }
//                        APP_ACTIVITY.navController.navigate(R.id.action_movielistFragment_to_moviesdetailFragment,bundle)
//                    }
//
//                }).apply {
//                    setMovie(appState.movie)
//                }
//*************** за комментировать adapter = ContainerAdapter( requireContext(), DataStore.populateData())
// если будем работать с  adapter = MovieListAdapter
                adapter = ContainerAdapter( requireContext(), DataStore.populateData())
//***************************************************************************************************
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentMovieList()
    }
}