package com.nat.cineandroid.core.api.nat.dto.cinemaRoom


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity

data class CinemaRoomPartialResponseDTO(
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
    val accessibility: Boolean
) {
    fun toCinemaRoomEntity(theaterId: Int): CinemaRoomEntity =
        CinemaRoomEntity(
            id = id,
            name = name,
            description = description,
            type = type,
            capacity = capacity,
            isAccessible = accessibility,
            theaterId = theaterId
        )
}