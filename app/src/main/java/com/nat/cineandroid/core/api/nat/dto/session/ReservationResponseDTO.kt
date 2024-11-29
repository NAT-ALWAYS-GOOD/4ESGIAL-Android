package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.session.ReservationEntity

data class ReservationResponseDTO(
    @SerializedName("reference")
    val reference: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("qrCode")
    val qrCode: String,
    @SerializedName("seats")
    val seats: List<Int>,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("session")
    val session: SessionPartialResponseDTO
) {
    fun toReservationEntity(): ReservationEntity =
        ReservationEntity(
            reference = reference,
            createdAt = java.time.Instant.parse(createdAt),
            qrCode = qrCode,
            seats = seats,
            userId = userId,
            sessionId = session.id
        )
}