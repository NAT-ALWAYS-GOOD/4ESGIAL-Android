package com.nat.cineandroid.ui.payment

import java.time.Instant

data class PaymentUiState(
    val movieTitle: String,
    val moviePosterUrl: String,
    val sessionStartTime: Instant,
    val theaterName: String,
    val seatNumbers: List<Int>,
    val totalPrice: Double
)
