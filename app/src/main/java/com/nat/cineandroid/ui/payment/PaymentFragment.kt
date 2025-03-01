package com.nat.cineandroid.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nat.cineandroid.R
import com.nat.cineandroid.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()
    private val args: PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        val theaterId = args.theaterId
        val sessionId = args.sessionId
        val selectedSeats = args.seatIds?.toList() ?: emptyList()

        viewModel.loadPaymentData(movieId, theaterId, sessionId, selectedSeats)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            state?.let { updateUI(it) }
        }

        binding.creditCardButton.setOnClickListener {
            // check if a payment method is selected
        }

        binding.paypalButton.setOnClickListener {
            // check if a payment method is selected
        }

        binding.googlePayButton.setOnClickListener {
            // check if a payment method is selected
        }

        // Bouton retour
        binding.backButton.root.setOnClickListener {
            findNavController().popBackStack()
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
        binding.sessionDateTime.text = "$date $time"
        binding.selectedSeatsText.text = "Seats: ${state.seatNumbers.joinToString(", ")}"

        binding.totalPriceText.text = "Total: ${state.totalPrice} €"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}