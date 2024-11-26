package com.nat.cineandroid.core.api.nat.dto.user


import com.google.gson.annotations.SerializedName

data class SignupRequestDTO(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)