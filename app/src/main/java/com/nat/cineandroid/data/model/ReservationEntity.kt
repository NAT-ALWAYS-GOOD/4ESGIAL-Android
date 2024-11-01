package com.nat.cineandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation")
data class ReservationEntity(
    @PrimaryKey val reference: String,
    val createdAt: String,
    val qrCode: String,
    val userId: Int
)
