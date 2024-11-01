package com.nat.cineandroid.data.model

import com.google.gson.annotations.SerializedName

data class ReservationResponse(
    @SerializedName("reference") val reference: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("qrCode") val qrCode: String
)