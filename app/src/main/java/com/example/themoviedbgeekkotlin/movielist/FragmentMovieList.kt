package com.example.themoviedbgeekkotlin.movielist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
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
import com.example.themoviedbgeekkotlin.storage.enteties.MovieEntity
import kotlinx.android.parcel.Parcelize

class FragmentMovieList : Fragment(), OnItemViewClickListener {

    private var _binding: FragmentMovieListFragmentBinding? = null
    private val binding get() = _binding!!
    private var adultSession: Boolean = false
    private var landSession: String = "ru"
    private var adapter2: MoviesCategoriesAdapter? = null

    private val viewModel: FragmentMovieListViewModel by viewModels { MoviesListViewModelFactory() }

    //переменная для хранения состояния доступности интернета
    var networkAvailable = false

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

        var urlIso3166 = "https://ru.wikipedia.org/wiki/ISO_3166-1"
        var urlIso639 = "https://snipp.ru/handbk/iso-639-1"
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(false)

        binding.searchLayout.iVHelp.setOnClickListener {
            loadWebSite(binding.webView, urlIso3166, requireContext())
        }

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
        }
    }

    override fun onStart() {
        super.onStart()
        initPreferences()
        viewModel.loadMoviesFromApi(landSession, adultSession)
    }

    private fun initPreferences() {

        adultSession = if (AppPreferences.getAdult()) {
            AppPreferences.getAdult()

        } else false

        landSession = if (AppPreferences.getLang()?.isNotEmpty() == true) {
            AppPreferences.getLang().toString()

        } else "ru"

        stateParamsStart()
    }

    // присвоение перед записьб в AppPreferences
    private fun stateParams() {
        if (binding.searchLayout.checkAdult.isChecked) {
            adultSession = true
        }

        if (binding.searchLayout.editTextSearch.text.isNotEmpty()) {
            landSession = binding.searchLayout.editTextSearch.text.toString()
        } else landSession = "ru"
    }

    // присвоение после инициализации AppPreferences
    private fun stateParamsStart() {
        if (adultSession == true) {
            binding.searchLayout.checkAdult.isChecked = true
        } else {
            binding.searchLayout.checkAdult.isChecked = false
        }

        if (landSession.isNotEmpty()) {
            binding.searchLayout.editTextSearch.append(landSession)
        } else {
            binding.searchLayout.editTextSearch.append("ru")
        }
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

    override fun onItemViewClick(movie: Movie) {
        // сохранение при клике по записи MovieList
//        viewModel.saveMoviesLocally(movie)
        val bundle = Bundle().also {
            it.putParcelable(BUNDLE_EXTRA, movie)
        }

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).also {
            it.navigate(R.id.moviesdetailFragment, bundle)
        }
    }

    override fun onItemViewClickNotes(movie: MovieEntity) {
    }

    // будет принимать webview, адрес сайта  и контекст, и будет загружать сайт
    private fun loadWebSite(webView: WebView, url: String, context: Context) {
        binding.progressBar.visibility = View.VISIBLE
        //Проверяем сетевое соединение
        networkAvailable = isNetworkAvailable(context)
        //очищаем кэш во избежание некоторых ошибок
        webView.clearCache(true)
        //Если соединение доступно, включаем видимость webview,
        // присваиваем ему кастомный WebViewClient, и загружаем сайт.
        // В случае отсутствия сети скрываем webview.
        if (networkAvailable) {
            // присваиваем ему кастоный WebViewClient
            wvVisible(webView)
            webView.loadUrl(url)
//            webView.webViewClient = MyWebViewClient()
        } else {
            wvGone(webView)
        }

    }

    //При наличии сети мы будем показывать webview и скрывать текстовое поле
    private fun wvVisible(mWebView: WebView) {
        mWebView.visibility = View.VISIBLE
    }

    //При отсутствии сети мы будем скрывать webview и отображать текстовое поле
    private fun wvGone(mWebView: WebView) {
        mWebView.visibility = View.GONE
//        tvCheckConnection.visibility = View.VISIBLE
        toastMessage("Возможная причина: Интернет-соединение отсутствует", requireContext())
        binding.progressBar.visibility = View.GONE
    }

    //для проверки доступа к сети
    //чтобы предупреждения устаревших методов не отображались
    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(context: Context): Boolean {
        try {
            //инициализируем ConnectivityManager
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //для версий до и после 23 нужно применять разные методы класса ConnectivityManager
            return if (Build.VERSION.SDK_INT > 22) {
                //Если версия больше 22, используем метод activeNetwork для получения информации
                // о сети по умолчанию, затем getNetworkCapabilities  для получения состояния
                // текущего соединения, и hasCapability для проверки соединения
                val an = cm.activeNetwork ?: return false
                val capabilities = cm.getNetworkCapabilities(an) ?: return false
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                //На более старых версиях будут использованы устаревшие методы activeNetworkInfo и isConnected
                val a = cm.activeNetworkInfo ?: return false
                a.isConnected && (a.type == ConnectivityManager.TYPE_WIFI || a.type == ConnectivityManager.TYPE_MOBILE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun toastMessage(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = FragmentMovieList()
    }
}