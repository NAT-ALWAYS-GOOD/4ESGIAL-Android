package com.nat.cineandroid.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nat.cineandroid.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailFragmentArgs by navArgs()

    private val viewModel: MovieDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId
        val theaterId = args.theaterId

        binding.backButton.root.setOnClickListener {
            val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToBillboardTab()
            binding.root.findNavController().navigate(action)
        }

        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding.movieTitle.text = movie.title
            binding.movieDescription.text = movie.description

            Glide.with(binding.root)
                .load(movie.imageUrl)
                .into(binding.backgroundPoster)
        }


        if (theaterId == -1) {
            binding.bookButton.visibility = View.GONE
        } else {
            binding.bookButton.visibility = View.VISIBLE
        }


        viewModel.fetchMovieById(movieId)
    }

}