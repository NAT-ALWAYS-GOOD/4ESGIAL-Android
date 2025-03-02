package com.nat.cineandroid.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nat.cineandroid.core.api.JwtTokenProvider
import com.nat.cineandroid.databinding.FragmentTheaterSelectionBottomSheetListDialogBinding
import com.nat.cineandroid.ui.SharedLocationViewModel
import com.nat.cineandroid.ui.home.HomeViewModel
import com.nat.cineandroid.ui.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TheaterSelectionBottomSheetFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var jwtTokenProvider: JwtTokenProvider

    private var _binding: FragmentTheaterSelectionBottomSheetListDialogBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val locationViewModel: SharedLocationViewModel by activityViewModels()
    private val userViewModel: UserViewModel by viewModels()

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

        val userId = isUserConnected()
        if (userId != null) {
            userViewModel.fetchUser(userId)
        }

        theaterAdapter = TheaterAdapter(
            onItemClicked = { theater ->
                locationViewModel.updateSelectedTheater(theater)
            },
            onFavoriteClick = {
                userViewModel.user.value?.let { user ->
                    userViewModel.updateFavoriteTheater(user.id, it.id)
                }
            }
        )

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            theaterAdapter.setUser(user)
        }

        binding.recyclerViewTheaters.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = theaterAdapter
        }

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

    private fun isUserConnected(): Int? {
        val token = jwtTokenProvider.getToken()
        if (token.isNullOrEmpty()) {
            return null
        }
        var userClaims = jwtTokenProvider.getUserClaimsFromJwt(token.toString())
        if (userClaims?.id.isNullOrEmpty() || userClaims.username.isNullOrEmpty()) {
            return null
        }
        return userClaims.id.toInt()
    }
}