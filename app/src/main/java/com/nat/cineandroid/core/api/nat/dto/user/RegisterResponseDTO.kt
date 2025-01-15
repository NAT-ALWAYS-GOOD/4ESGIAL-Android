package com.nat.cineandroid.core.api.nat.dto.user


import com.google.gson.annotations.SerializedName

data class RegisterResponseDTO(
    @SerializedName("user")
    val user: UserResponseDTO,
    @SerializedName("access_token")
    val accessToken: String
)