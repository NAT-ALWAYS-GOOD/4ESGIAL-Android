package com.nat.cineandroid.core.api.nat.dto.theater


import com.google.gson.annotations.SerializedName

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
)