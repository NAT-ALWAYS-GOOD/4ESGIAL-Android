package com.nat.cineandroid.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private var movieYoutubeId: String? = null
    private val viewModel: MovieDetailViewModel by viewModels()

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
            view.findNavController().popBackStack()
        }

        binding.trailerButton.isEnabled = false

        binding.trailerButton.setOnClickListener {
            movieYoutubeId?.let { trailerId ->
                val trailerDialog = TrailerDialogFragment.newInstance(trailerId)
                trailerDialog.show(childFragmentManager, "trailer_dialog")
            }
        }

        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding.movieTitle.text = movie.title
            binding.movieDescription.text = movie.description

            Glide.with(binding.root)
                .load(movie.imageUrl)
                .into(binding.backgroundPoster)

            movieYoutubeId = movie.trailerYoutubeId
            binding.trailerButton.isEnabled = true
        }


        if (theaterId == -1) {
            binding.bookButton.visibility = View.GONE
        } else {
            binding.bookButton.visibility = View.VISIBLE
        }


        viewModel.fetchMovieById(movieId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}