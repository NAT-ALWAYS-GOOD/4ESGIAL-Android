package com.nat.cineandroid.data.session.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "seat",
    primaryKeys = ["seatNumber", "sessionId"],
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId")]
)
data class SeatEntity(
    val seatNumber: Int,
    val sessionId: Int,
    val isReserved: Boolean
)
