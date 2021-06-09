package com.example.themoviedbgeekkotlin.movielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademy.adapter.MoviesCategoriesAdapter
import com.example.themoviedbgeekkotlin.databinding.FragmentMovieListFragmentBinding
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.model.MovieGroup
import com.example.themoviedbgeekkotlin.movielist.sectionrecyclerview.ContainerAdapter
import com.example.themoviedbgeekkotlin.notification.MoviesNotificationHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import ru.androidschool.groupiesample.items.MainCardContainer
import ru.androidschool.groupiesample.items.MovieItem

class FragmentMovieList : Fragment() {

    private var _binding: FragmentMovieListFragmentBinding? = null
    private val binding get() = _binding!!

//    private var adapter: ContainerAdapter? =null

    private var adapter: MoviesCategoriesAdapter? =null

    // инииализация без Factory
//    private val viewModel: FragmentMovieListViewModel by lazy {
//        ViewModelProvider(this).get(FragmentMovieListViewModel::class.java)
//    }

    private val viewModel: FragmentMovieListViewModel by viewModels { MoviesListViewModelFactory() }

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
        val moviesRecyclerView: RecyclerView = binding.movieListRecyclerView
        moviesRecyclerView.adapter = adapter
        // отображаем нотификацию
        showNotification()

//        init()
       setObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.updateDate()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateDate()
    }

    private fun setObservers() {
        // observe movies data
        viewModel.listMovies.observe(viewLifecycleOwner, {
//            (binding.movieListRecyclerView.adapter as MoviesCategoriesAdapter).apply {
            (adapter)?.apply {
                val movies = it ?: return@observe
                setMovie(movies)
                notifyDataSetChanged()
        //                val movies = it ?: return@observe
        //                addItems(movies as ArrayList<MovieGroup>)
        //                notifyDataSetChanged()
            }
        })

        // observe status
        viewModel.state.observe(viewLifecycleOwner, { status ->
            when (status) {
                is AppState.Init, is AppState.Success -> {
                    binding.progressBar?.visibility = View.INVISIBLE
                }
                is AppState.Loading -> {
                    binding.progressBar?.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    binding.progressBar?.visibility = View.INVISIBLE
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNotification() {
        // запустим уведомление
        //Теперь, по нажатию на кнопку, помимо перехода на следующий фрагмент у нас появится уведомление
        MoviesNotificationHelper.createMoviesNotification(
                requireContext(), "Супер Уведомление", "Это уведомление для отладки", "", true
        )

    }

    companion object {
        fun newInstance() = FragmentMovieList()
    }
}