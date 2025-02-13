package com.nat.cineandroid.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nat.cineandroid.R
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import com.nat.cineandroid.databinding.FragmentMapsBinding
import com.nat.cineandroid.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private val locationViewModel: SharedLocationViewModel by activityViewModels()
    private var googleMapInstance: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        googleMapInstance = googleMap
        observeSelectedTheater()
        observeTheaters()
    }

    private fun observeTheaters() {
        viewModel.theaters.observe(viewLifecycleOwner) { theaters ->
            Log.d("MapsFragment", "Theaters received: $theaters")
            if (!theaters.isNullOrEmpty()) {
                addMarkersOnMap(theaters)
            }
        }
    }

    private fun addMarkersOnMap(theaters: List<TheaterEntity>) {
        googleMapInstance?.let { googleMap ->
            googleMap.clear()

            theaters.forEach { theater ->
                val position = LatLng(theater.latitude, theater.longitude)
                val marker = googleMap.addMarker(
                    MarkerOptions().position(position).title(theater.name)
                )
                marker?.tag = theater.id
            }
        }
    }

    private fun observeSelectedTheater() {
        locationViewModel.selectedTheater.observe(viewLifecycleOwner) { theater ->
            theater?.let {
                val position = LatLng(it.latitude, it.longitude)
                googleMapInstance?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12f))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.root.setOnClickListener {
            val action = MapsFragmentDirections.actionMapsFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}