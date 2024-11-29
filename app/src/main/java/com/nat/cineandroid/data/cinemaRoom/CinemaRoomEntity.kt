package com.nat.cineandroid.data.cinemaRoom

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nat.cineandroid.data.theater.TheaterEntity

@Entity(
    tableName = "cinema_room",
    foreignKeys = [ForeignKey(
        entity = TheaterEntity::class,
        parentColumns = ["id"],
        childColumns = ["theaterId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("theaterId")]
)
data class CinemaRoomEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val capacity: Int,
    val isAccessible: Boolean,
    val theaterId: Int? = null
)
