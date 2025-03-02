package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.data.session.entity.SessionEntity

data class SessionPartialResponseWithoutMovieDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("room")
    val room: CinemaRoomPartialResponseDTO
) {
    fun toSessionEntity(movieId: Int): SessionEntity =
        SessionEntity(
            id = id,
            startTime = java.time.Instant.parse(startTime),
            endTime = java.time.Instant.parse(endTime),
            movieId = movieId,
            roomId = room.id
        )
}
