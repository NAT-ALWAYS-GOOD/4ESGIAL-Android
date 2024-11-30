package com.nat.cineandroid.core.api.nat.dto.movie


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.movie.entity.MovieScheduleEntity

data class MovieScheduleResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("movie")
    val movie: MovieResponseDTO
) {
    fun toMovieScheduleEntity(): MovieScheduleEntity =
        MovieScheduleEntity(
            id = id,
            startTime = java.time.Instant.parse(startTime),
            endTime = java.time.Instant.parse(endTime),
            movieId = movie.id
        )
}