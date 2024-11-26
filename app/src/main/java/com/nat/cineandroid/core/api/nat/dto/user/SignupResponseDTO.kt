package com.nat.cineandroid.core.api.nat.dto.user


import com.google.gson.annotations.SerializedName

data class SignupResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String
)