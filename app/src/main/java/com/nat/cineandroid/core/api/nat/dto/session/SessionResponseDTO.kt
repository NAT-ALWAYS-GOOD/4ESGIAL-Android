package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.data.session.entity.SessionEntity

data class SessionResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("movie")
    val movie: MovieResponseDTO,
    @SerializedName("room")
    val room: CinemaRoomResponseDTO,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("seats")
    val seats: List<SeatResponseDTO>
) {
    fun toSessionEntity(): SessionEntity =
        SessionEntity(
            id = id,
            startTime = java.time.Instant.parse(startTime),
            endTime = java.time.Instant.parse(endTime),
            movieId = movie.id,
            roomId = room.id
        )

    fun toSeatEntities(): List<SeatEntity> =
        seats.map { it ->
            SeatEntity(
                seatNumber = it.seatNumber,
                sessionId = id,
                isReserved = it.isReserved
            )
        }
}