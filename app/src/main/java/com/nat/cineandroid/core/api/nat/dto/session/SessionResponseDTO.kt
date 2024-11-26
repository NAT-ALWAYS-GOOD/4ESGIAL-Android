package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import java.time.Instant

data class SessionResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("movie")
    val movie: MovieResponseDTO,
    @SerializedName("room")
    val room: CinemaRoomResponseDTO,
    @SerializedName("startTime")
    val startTime: Instant,
    @SerializedName("endTime")
    val endTime: Instant,
    @SerializedName("seats")
    val seats: List<SeatResponseDTO>
)