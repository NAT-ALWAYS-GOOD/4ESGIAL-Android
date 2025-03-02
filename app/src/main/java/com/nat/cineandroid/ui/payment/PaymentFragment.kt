package com.nat.cineandroid.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nat.cineandroid.core.api.JwtTokenProvider
import com.nat.cineandroid.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    @Inject
    lateinit var jwtTokenProvider: JwtTokenProvider

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()
    private val args: PaymentFragmentArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        val userId = isUserConnected()
        if (userId == null) {
            val action = PaymentFragmentDirections.actionPaymentFragmentToLoginFragment()
            navController.navigate(action)
            return
        }

        val movieId = args.movieId
        val theaterId = args.theaterId
        val sessionId = args.sessionId
        val selectedSeats = args.seatIds?.toList() ?: emptyList()

        viewModel.loadPaymentData(movieId, theaterId, sessionId, selectedSeats)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            state?.let { updateUI(it) }
        }

        binding.creditCardButton.setOnClickListener {
            viewModel.reserveSeats(userId, sessionId, selectedSeats)
        }

        binding.paypalButton.setOnClickListener {
            viewModel.reserveSeats(userId, sessionId, selectedSeats)
        }

        binding.googlePayButton.setOnClickListener {
            viewModel.reserveSeats(userId, sessionId, selectedSeats)
        }

        viewModel.reservation.observe(viewLifecycleOwner) { reservation ->
            val action = PaymentFragmentDirections.actionPaymentFragmentToProfileFragment()
            navController.navigate(action)
        }

        // Bouton retour
        binding.backButton.root.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun updateUI(state: PaymentUiState) {
        // Charger l'affiche du film (par exemple, avec Glide)
        Glide.with(this)
            .load(state.moviePosterUrl)
            .into(binding.moviePoster)

        binding.movieTitle.text = state.movieTitle
        binding.theaterName.text = state.theaterName
        val date = viewModel.extractDate(state.sessionStartTime)
        val time = viewModel.extractTime(state.sessionStartTime)
        binding.sessionDateTime.text = getString(com.nat.cineandroid.R.string.session_date, date, time)
        binding.selectedSeatsText.text = getString(com.nat.cineandroid.R.string.seats_selected, state.seatNumbers.joinToString(", "))

        binding.totalPriceText.text = getString(com.nat.cineandroid.R.string.total_price, state.totalPrice)
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