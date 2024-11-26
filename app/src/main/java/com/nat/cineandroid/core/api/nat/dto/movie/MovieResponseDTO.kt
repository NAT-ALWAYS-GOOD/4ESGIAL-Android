package com.nat.cineandroid.core.api.nat.dto.movie


import com.google.gson.annotations.SerializedName
import java.time.Instant

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
    val releaseDate: Instant,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("trailerYoutubeId")
    val trailerYoutubeId: String
)