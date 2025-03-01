package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.session.entity.ReservationEntity
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import java.time.Instant

data class ReservationWithoutTheaterResponseDTO(
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
    val session: SessionPartialResponseDTO,
    @SerializedName("theaterEntity")
    val theaterEntity: TheaterEntity?
) {
    fun toReservationEntity(): ReservationEntity =
        ReservationEntity(
            reference = reference,
            createdAt = Instant.parse(createdAt),
            qrCode = qrCode,
            seats = seats,
            userId = userId,
            sessionId = session.id
        )
}