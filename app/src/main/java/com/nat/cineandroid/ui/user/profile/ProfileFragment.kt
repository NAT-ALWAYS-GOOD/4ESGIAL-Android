package com.nat.cineandroid.ui.user.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nat.cineandroid.core.api.JwtTokenProvider
import com.nat.cineandroid.databinding.FragmentProfileBinding
import com.nat.cineandroid.ui.MovieDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject lateinit var jwtTokenProvider: JwtTokenProvider
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    private lateinit var upcomingAdapter: ReservationAdapter
    private lateinit var pastAdapter: ReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val token = jwtTokenProvider.getToken()
        if (token.isNullOrEmpty()) {
            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            navController.navigate(action)
            return
        }
        val userClaims = jwtTokenProvider.getUserClaimsFromJwt(token)
        if (userClaims?.id.isNullOrEmpty() || userClaims.username.isNullOrEmpty()) {
            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            navController.navigate(action)
            return
        }
        binding.welcomeMessage.text = "Bienvenue, ${userClaims.username}!"

        viewModel.reservations.observe(viewLifecycleOwner) { reservations ->
            Log.d("ProfileFragment", "Reservations: $reservations")
        }

        upcomingAdapter = ReservationAdapter { reservation ->
            Log.d("ProfileFragment", "Reservation clicked: $reservation")
            val qrCodeDialog = QRCodeDialogFragment.newInstance(reservation.reservation.qrCode)
            qrCodeDialog.show(childFragmentManager, "qr_code_dialog")
        }

        pastAdapter = ReservationAdapter { reservation ->
            Log.d("ProfileFragment", "Reservation clicked: $reservation")
        }

        binding.reservationsList.adapter = upcomingAdapter
        binding.reservationsHistoryList.adapter = pastAdapter

        viewModel.upcomingReservations.observe(viewLifecycleOwner) { reservations ->
            upcomingAdapter.submitList(reservations)
        }

        viewModel.pastReservations.observe(viewLifecycleOwner) { reservations ->
            pastAdapter.submitList(reservations)
        }

        binding.logoutButton.setOnClickListener {
            jwtTokenProvider.clearToken()
            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            navController.navigate(action)
        }

        viewModel.fetchReservations(userClaims.id.toInt())
    }
}