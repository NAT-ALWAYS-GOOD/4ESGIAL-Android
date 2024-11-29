package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.session.SeatEntity

data class SeatResponseDTO(
    @SerializedName("seatNumber")
    val seatNumber: Int,
    @SerializedName("reserved")
    val reserved: Boolean
)