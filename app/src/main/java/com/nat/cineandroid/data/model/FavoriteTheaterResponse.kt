package com.nat.cineandroid.data.model

import com.google.gson.annotations.SerializedName

data class FavoriteTheaterResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("streetName") val streetName: String,
    @SerializedName("streetNumber") val streetNumber: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String
) {
    fun toFavoriteTheaterEntity(): FavoriteTheaterEntity {
        return FavoriteTheaterEntity(
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
}