package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import java.time.Instant

data class ReservationResponseDTO(
    @SerializedName("reference")
    val reference: String,
    @SerializedName("createdAt")
    val createdAt: Instant,
    @SerializedName("qrCode")
    val qrCode: String,
    @SerializedName("seats")
    val seats: List<Int>,
    @SerializedName("user")
    val user: String,
    @SerializedName("session")
    val session: SessionPartialResponseDTO
)