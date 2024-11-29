package com.nat.cineandroid.core.api.nat.dto.theater


import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.data.theater.TheaterEntity

data class TheaterResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("streetName")
    val streetName: String,
    @SerializedName("streetNumber")
    val streetNumber: String,
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
) {
    fun toTheaterEntity(): TheaterEntity =
        TheaterEntity(
            id = id,
            name = name,
            description = description,
            streetName = streetName,
            streetNumber = streetNumber,
            postalCode = postalCode,
            city = city,
            country = country,
            latitude = latitude,
            longitude = longitude
        )
}