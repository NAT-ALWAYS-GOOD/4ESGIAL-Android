package com.nat.cineandroid.core.api.nat.dto.movie


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.movie.MovieEntity

data class MovieResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("trailerYoutubeId")
    val trailerYoutubeId: String,
    @SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toMovieEntity(): MovieEntity =
        MovieEntity(
            id = id,
            title = title,
            description = description,
            duration = duration,
            releaseDate = java.time.Instant.parse(releaseDate),
            isActive = isActive,
            trailerYoutubeId = trailerYoutubeId,
            imageUrl = imageUrl
        )
}