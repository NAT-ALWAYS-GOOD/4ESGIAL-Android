package com.nat.cineandroid.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val password: String,
    val isActive: Boolean,
    @Embedded(prefix = "favorite_") val favoriteTheater: FavoriteTheaterEntity?
)