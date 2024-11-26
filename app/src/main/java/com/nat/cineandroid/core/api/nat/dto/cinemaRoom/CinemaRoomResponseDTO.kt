package com.nat.cineandroid.core.api.nat.dto.cinemaRoom


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO

data class CinemaRoomResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("accessibility")
    val accessibility: Boolean,
    @SerializedName("theater")
    val theater: TheaterResponseDTO
)