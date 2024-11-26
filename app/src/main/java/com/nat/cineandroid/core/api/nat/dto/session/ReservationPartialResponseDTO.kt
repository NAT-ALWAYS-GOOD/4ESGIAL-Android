package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName

data class ReservationPartialResponseDTO(
    @SerializedName("reference")
    val reference: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("qrCode")
    val qrCode: String
)