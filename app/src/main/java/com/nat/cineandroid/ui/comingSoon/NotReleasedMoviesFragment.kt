package com.nat.cineandroid.ui.comingSoon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nat.cineandroid.databinding.FragmentNotReleasedMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotReleasedMoviesFragment : Fragment() {

    private var _binding: FragmentNotReleasedMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotReleasedMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesAdapter = MoviesAdapter { movie ->
            val action = NotReleasedMoviesFragmentDirections
                .actionNotReleasedMoviesFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)
        }
        binding.recyclerNotReleasedMovies.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel.unreleasedMovies.observe(viewLifecycleOwner) { movies ->
            Log.d("NotReleasedMoviesFragment", "Movies: $movies")
            moviesAdapter.submitList(movies)
        }

        viewModel.fetchUnreleasedMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
