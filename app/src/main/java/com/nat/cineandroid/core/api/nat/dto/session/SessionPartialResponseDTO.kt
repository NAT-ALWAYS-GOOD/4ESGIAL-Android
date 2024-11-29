package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import com.nat.cineandroid.data.session.SessionEntity

data class SessionPartialResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("movie")
    val movie: MovieResponseDTO,
    @SerializedName("room")
    val room: CinemaRoomPartialResponseDTO
) {
    fun toSessionEntity(): SessionEntity =
        SessionEntity(
            id = id,
            startTime = java.time.Instant.parse(startTime),
            endTime = java.time.Instant.parse(endTime),
            movieId = movie.id,
            roomId = room.id
        )
}