package com.nat.cineandroid.data.user.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nat.cineandroid.data.theater.entity.TheaterEntity

@Entity(
    tableName = "user",
    foreignKeys = [ForeignKey(
        entity = TheaterEntity::class,
        parentColumns = ["id"],
        childColumns = ["favoriteTheaterId"],
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index("favoriteTheaterId")],
)
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val isActive: Boolean,
    val favoriteTheaterId: Int? = null
)
