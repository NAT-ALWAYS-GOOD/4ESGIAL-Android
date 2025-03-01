package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName

data class SeatResponseDTO(
    @SerializedName("seatNumber")
    val seatNumber: Int,
    @SerializedName("isReserved")
    val isReserved: Boolean
)