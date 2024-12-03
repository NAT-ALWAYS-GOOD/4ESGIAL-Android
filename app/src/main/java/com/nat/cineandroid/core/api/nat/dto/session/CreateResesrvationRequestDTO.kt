package com.nat.cineandroid.core.api.nat.dto.session


import com.google.gson.annotations.SerializedName

data class CreateResesrvationRequestDTO(
    @SerializedName("sessionId")
    val sessionId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("seats")
    val seats: List<Int>
)