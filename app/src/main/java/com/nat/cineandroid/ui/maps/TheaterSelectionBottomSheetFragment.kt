package com.nat.cineandroid.ui.maps

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nat.cineandroid.R
import com.nat.cineandroid.databinding.FragmentTheaterSelectionBottomSheetListDialogItemBinding
import com.nat.cineandroid.databinding.FragmentTheaterSelectionBottomSheetListDialogBinding
import com.nat.cineandroid.ui.SharedLocationViewModel
import com.nat.cineandroid.ui.home.HomeViewModel

class TheaterSelectionBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTheaterSelectionBottomSheetListDialogBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val locationViewModel: SharedLocationViewModel by activityViewModels()

    private lateinit var theaterAdapter: TheaterAdapter

    companion object {
        private const val ARG_INITIAL_THEATER_ID = "initial_theater_id"
        fun newInstance(initialTheaterId: Int): TheaterSelectionBottomSheetFragment {
            val args = Bundle().apply {
                putInt(ARG_INITIAL_THEATER_ID, initialTheaterId)
            }
            return TheaterSelectionBottomSheetFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentTheaterSelectionBottomSheetListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theaterAdapter = TheaterAdapter { theater ->
            // Quand l'utilisateur clique sur un item, on met à jour le cinéma sélectionné.
            locationViewModel.updateSelectedTheater(theater)
        }

        // Configuration de la RecyclerView
        binding.recyclerViewTheaters.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = theaterAdapter
        }

        // Observer la liste des cinémas depuis HomeViewModel
        homeViewModel.theaters.observe(viewLifecycleOwner) { theaters ->
            theaterAdapter.submitList(theaters)

            arguments?.getInt(ARG_INITIAL_THEATER_ID)?.let { initialTheaterId ->
                theaterAdapter.setSelectedTheater(initialTheaterId)
                val initialTheaterIndex = theaterAdapter.getSelectedIndex()
                binding.recyclerViewTheaters.post {
                    binding.recyclerViewTheaters.scrollToPosition(initialTheaterIndex)
                }
            }
        }

        binding.buttonConfirm.setOnClickListener {
            theaterAdapter.getSelectedTheater()?.let { selectedTheater ->
                locationViewModel.updateSelectedTheater(selectedTheater)
            }
            dismiss()
            val action = MapsFragmentDirections.Companion.actionMapsFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}