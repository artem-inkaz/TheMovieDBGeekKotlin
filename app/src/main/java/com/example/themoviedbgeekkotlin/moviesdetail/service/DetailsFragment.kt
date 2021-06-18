package com.example.themoviedbgeekkotlin.moviesdetail.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themoviedbgeekkotlin.APP_ACTIVITY
import com.example.themoviedbgeekkotlin.BuildConfig
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.api.MovieDtoEx
import com.example.themoviedbgeekkotlin.databinding.FragmentDetailsBinding
import com.example.themoviedbgeekkotlin.model.Movie
import com.example.themoviedbgeekkotlin.movielist.FragmentMovieListViewModel
import com.example.themoviedbgeekkotlin.moviesdetail.ActorAdapter

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"

const val DETAILS_ID_EXTRA = "ID"
const val DETAILS_TITLE_EXTRA = "TITLE"
const val DETAILS_STORY_EXTRA = "STORY"
const val DETAILS_BACKDROP_EXTRA = "BACKDROP"
const val DETAILS_RATINGS_EXTRA = "RATINGS"
const val DETAILS_RUNTIME_EXTRA = "RUNTIME"
const val DETAILS_REVIEWS_EXTRA = "REVIEWS"


//private const val TEMP_INVALID = -100
//private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieBundle: Movie

    private val viewModel: FragmentMovieListViewModel by lazy {
        ViewModelProvider(this).get(FragmentMovieListViewModel::class.java)
    }
    private var adapter: ActorAdapter? = null

    //Создаём свой BroadcastReceiver (получатель широковещательного сообщения)
    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

//   в интенте броадкаста всегда отправлялся флаг, указывающий на результат работы сервиса:
//   во фрагменте через when мы сможем прочитать и обработать результат.
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    MovieDtoEx(
//                        FactDTO(
                        intent.getIntExtra(DETAILS_ID_EXTRA, 632357),
                        intent.getStringExtra(DETAILS_TITLE_EXTRA),
                        intent.getStringExtra(DETAILS_STORY_EXTRA),
                        intent.getStringExtra(DETAILS_BACKDROP_EXTRA),
                        intent.getFloatExtra(DETAILS_RATINGS_EXTRA, 0.0f),
                        intent.getIntExtra(DETAILS_RUNTIME_EXTRA, 0),
                        intent.getIntExtra(DETAILS_REVIEWS_EXTRA, 0),

                        )
//                    )
                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        movieBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Movie()

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        getMovie()

        binding.toolbar.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_detailsFragment_to_movielistFragment)
        }

//        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderDataViewModel(it) })
//        viewModel.getMovieFromLocalStorage()
    }

    private fun getMovie() {
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(
                    MOVIEID_EXTRA,
                    movieBundle.id
                )
            })
        }
    }

    private fun renderData(movieDtoEx: MovieDtoEx) {

        with(binding) {
            tvTitle.text = movieDtoEx.title
            Glide.with(root.context)
                .load(BuildConfig.BASE_IMAGE_URL + movieDtoEx.backdrop)
                .apply(imageOption)
                .into(imgTitlePoster)
            ratingBar.rating = movieDtoEx.ratings / 2
            tvReviews.text = movieDtoEx.reviews.toString() + " REVIEWS"
            tvStorylineText.text = movieDtoEx.overview
        }
    }

    // Статус загрузки
//    private fun renderDataViewModel(appState: AppState) {
//
//        when (appState) {
//            is AppState.Success -> {
//                adapter = ActorAdapter()
//                        .apply { setActor(appState.movie) }
//                binding.recyclerView.adapter = adapter
//                binding.recyclerView.adapter?.notifyDataSetChanged()
//            }
//            is AppState.Loading -> {
//            }
//            is AppState.Error -> {
//                Snackbar
//                        .make(binding.root, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
//                        .setAction(getString(R.string.reload)) { viewModel.getMovieFromLocalStorage() }
//                        .show()
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    companion object {

        const val BUNDLE_EXTRA = "movie"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }

        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_combined_shape)
            .fallback(R.drawable.ic_combined_shape)
            .centerCrop()
    }
}

