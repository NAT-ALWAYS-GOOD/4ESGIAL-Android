package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.session.ReservationEntity

data class ReservationPartialResponseDTO(
    @SerializedName("reference")
    val reference: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("qrCode")
    val qrCode: String
) {
    fun toReservationEntity(): ReservationEntity =
        ReservationEntity(
            reference = reference,
            createdAt = java.time.Instant.parse(createdAt),
            qrCode = qrCode
        )
}