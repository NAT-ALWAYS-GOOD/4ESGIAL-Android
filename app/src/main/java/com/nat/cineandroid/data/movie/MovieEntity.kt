package com.nat.cineandroid.data.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val releaseDate: Instant,
    val isActive: Boolean,
    val trailerYoutubeId: String,
    val imageUrl: String
)
