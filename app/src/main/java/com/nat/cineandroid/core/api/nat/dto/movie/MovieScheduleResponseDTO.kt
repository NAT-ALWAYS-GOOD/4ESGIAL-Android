package com.nat.cineandroid.core.api.nat.dto.movie


import com.google.gson.annotations.SerializedName
import java.time.Instant

data class MovieScheduleResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("startTime")
    val startTime: Instant,
    @SerializedName("endTime")
    val endTime: Instant,
    @SerializedName("movie")
    val movie: MovieResponseDTO
)