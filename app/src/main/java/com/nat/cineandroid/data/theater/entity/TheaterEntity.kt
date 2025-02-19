package com.nat.cineandroid.data.theater.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theater")
data class TheaterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val streetName: String,
    val streetNumber: String,
    val postalCode: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)
