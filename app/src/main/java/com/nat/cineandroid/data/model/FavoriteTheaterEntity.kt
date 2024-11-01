package com.nat.cineandroid.data.model

data class FavoriteTheaterEntity(
    val id: Int,
    val name: String,
    val description: String,
    val streetName: String,
    val streetNumber: String,
    val postalCode: String,
    val city: String,
    val country: String,
    val latitude: String,
    val longitude: String
)
