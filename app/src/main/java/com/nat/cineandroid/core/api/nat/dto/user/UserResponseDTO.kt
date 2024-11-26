package com.nat.cineandroid.core.api.nat.dto.user


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.session.ReservationPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO

data class UserResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("reservations")
    val reservations: List<ReservationPartialResponseDTO>,
    @SerializedName("favoriteTheater")
    val favoriteTheater: TheaterResponseDTO
)