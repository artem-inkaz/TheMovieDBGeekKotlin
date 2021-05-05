package com.example.themoviedbgeekkotlin.moviesdetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.themoviedbgeekkotlin.R
import com.example.themoviedbgeekkotlin.databinding.FragmentMovieListFragmentBinding
import com.example.themoviedbgeekkotlin.databinding.FragmentMoviesDetailsFragmentBinding

class FragmentMoviesDetails : Fragment() {

    private var _binding: FragmentMoviesDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FragmentMoviesDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesDetailsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentMoviesDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentMoviesDetails()
    }

}