package com.nat.cineandroid.data.session.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "reservation",
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
data class ReservationEntity(
    @PrimaryKey val reference: String,
    val createdAt: Instant,
    val qrCode: String,
    val seats: List<Int>? = null,
    val userId: Int? = null,
    val sessionId: Int? = null
)
