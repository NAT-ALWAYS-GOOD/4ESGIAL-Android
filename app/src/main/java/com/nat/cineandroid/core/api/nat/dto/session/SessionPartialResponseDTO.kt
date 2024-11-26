package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO

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
)