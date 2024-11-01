package com.nat.cineandroid.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("reservations") val reservations: List<ReservationResponse>,
    @SerializedName("favoriteTheater") val favoriteTheater: FavoriteTheaterResponse?
) {
    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            password = password,
            isActive = isActive,
            favoriteTheater = favoriteTheater?.toFavoriteTheaterEntity()
        )
    }
}