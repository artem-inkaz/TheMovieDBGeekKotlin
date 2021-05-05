package com.example.themoviedbgeekkotlin.favourite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.themoviedbgeekkotlin.databinding.FragmentFavouriteFragmentBinding

class FragmentFavourite : Fragment() {

    private var _binding: FragmentFavouriteFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FragmentFavouriteViewModel by lazy {
        ViewModelProvider(this).get(FragmentFavouriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FragmentFavourite()
    }

}