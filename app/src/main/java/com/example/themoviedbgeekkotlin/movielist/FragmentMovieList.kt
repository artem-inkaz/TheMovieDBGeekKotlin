package com.example.themoviedbgeekkotlin.movielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbgeekkotlin.App
import com.example.themoviedbgeekkotlin.AppPreferences
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMovieListFragmentBinding
import com.example.themoviedbgeekkotlin.interfaces.OnItemViewClickListener
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.sectionrecyclerviewv2.MoviesCategoriesAdapter
import com.example.themoviedbgeekkotlin.moviesdetail.FragmentMoviesDetails.Companion.BUNDLE_EXTRA
import com.example.themoviedbgeekkotlin.notification.MoviesNotificationHelper
//import kotlinx.android.synthetic.main.fragment_movie_list_fragment.*

class FragmentMovieList : Fragment(), OnItemViewClickListener {

    private var _binding: FragmentMovieListFragmentBinding? = null
    private val binding get() = _binding!!

    private var adultSession: Boolean = false
    private var landSession: String = "ru"


    // вариант ленивой инициализаии
    private val adapterMoviesGroup by lazy { MoviesCategoriesAdapter(this) }

    private var adapter2: MoviesCategoriesAdapter? = null

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

        adapter2 = MoviesCategoriesAdapter(this)
        moviesRecyclerView.adapter = adapter2

        SearchBySetting()
        // отображаем нотификацию
        showNotification()
        stateParams()
        setObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
//            initPreferences()
//            viewModel.loadMoviesFromApi(landSession,adultSession)
        }
    }

    override fun onStart() {
        super.onStart()
        initPreferences()
        viewModel.loadMoviesFromApi(landSession,adultSession)
    }

    private fun initPreferences(){
        adultSession = if(AppPreferences.getAdult()) {
            AppPreferences.getAdult()
        } else false

        landSession = if(AppPreferences.getLang()?.isNotEmpty() == true) {
            AppPreferences.getLang().toString()
        } else "ru"

        stateParamsStart()
    }
    // присвоение перед записьб в AppPreferences
    private fun stateParams() {
        if (binding.searchLayout.checkAdult.isChecked)  adultSession = true
        if (binding.searchLayout.editTextSearch.text.isNotEmpty())
            landSession = binding.searchLayout.editTextSearch.text.toString()
        else landSession = "ru"
    }
    // присвоение после инициализации AppPreferences
    private fun stateParamsStart() {
        if (adultSession == true) binding.searchLayout.checkAdult.isChecked = true
        else binding.searchLayout.checkAdult.isChecked = false

        if (landSession.isNotEmpty()) binding.searchLayout.editTextSearch.append(landSession)
        else binding.searchLayout.editTextSearch.append("ru")
    }


    private fun SearchBySetting() {
        binding.searchLayout.searchButton.setOnClickListener {
        stateParams()
        AppPreferences.setAdult(adultSession)
        AppPreferences.setLang(landSession)
        viewModel.loadMoviesFromApi(landSession, adultSession)
            setObservers()
    }
    }


    private fun setObservers() {
        // observe movies data
        viewModel.listMovies.observe(viewLifecycleOwner, {
            val movies = it ?: return@observe
            movies.let {
                adapter2?.setMovie(movies)
                adapter2?.notifyDataSetChanged()
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

    override fun onItemViewClick(movie: Movie) {
        val bundle = Bundle().also {
            it.putParcelable(BUNDLE_EXTRA, movie)
        }

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
            it.navigate(R.id.moviesdetailFragment, bundle)
        }
    }
}