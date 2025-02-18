package com.nat.cineandroid.ui.home

import android.content.Context
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nat.cineandroid.databinding.FragmentHomeBinding
import com.nat.cineandroid.ui.SharedLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private val locationViewModel: SharedLocationViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                locationViewModel.requestUserLocation()
            } else {
                selectDefaultTheater()
            }
        }

    private lateinit var moviePagerAdapter: MoviePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: if nothing is returned, show a message to the user

        checkLocationPermission()

        binding.favCinemaButton.root.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMapsFragment()
            findNavController().navigate(action)
        }

        moviePagerAdapter = MoviePagerAdapter { movieWithSessions ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(
                    movieWithSessions.movie.id,
                    theaterId = 1
                )
            findNavController().navigate(action)
        }

        binding.movieViewPager.adapter = moviePagerAdapter

        viewModel.theaters.observe(viewLifecycleOwner) { theaters ->
            Log.d("HomeFragment", "Theaters received: ${theaters.size}")
            locationViewModel.selectClosestTheater(theaters)
            viewModel.fetchMoviesWithSessions(theaterId = locationViewModel.selectedTheater.value!!.id)
        }

        viewModel.moviesWithSessions.observe(viewLifecycleOwner) { moviesWithSession ->
            if (moviesWithSession.isNullOrEmpty()) {
                Log.d("HomeFragment", "No moviesWithSession received")
            } else {
                Log.d("HomeFragment", "Movies received: ${moviesWithSession.size}")
            }
            moviePagerAdapter.submitList(moviesWithSession)
        }

        locationViewModel.selectedTheater.observe(viewLifecycleOwner) { theater ->
            binding.favCinemaButton.cinemaName.text = theater.name
        }

        TabLayoutMediator(binding.tabLayout, binding.movieViewPager) { tab, position ->
            tab.contentDescription = "Film de la page ${position + 1}"
        }.attach()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.searchButton.root.setOnClickListener() {
            binding.searchButton.root.visibility = View.GONE
            binding.searchBarComponent.root.visibility = View.VISIBLE
            binding.searchBarComponent.searchEditText.requestFocus()
            imm.showSoftInput(
                binding.searchBarComponent.searchEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        binding.root.setOnClickListener {
            if (binding.searchBarComponent.root.visibility == View.VISIBLE) {
                binding.searchBarComponent.searchEditText.clearFocus()
            }
        }

        binding.searchBarComponent.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.searchButton.root.visibility = View.VISIBLE
                binding.searchBarComponent.root.visibility = View.GONE

                imm.hideSoftInputFromWindow(
                    binding.searchBarComponent.searchEditText.windowToken,
                    0
                )
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.fetchTheaters()
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationViewModel.requestUserLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun selectDefaultTheater() {
        viewModel.theaters.value?.firstOrNull()?.let {
            locationViewModel.updateSelectedTheater(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}