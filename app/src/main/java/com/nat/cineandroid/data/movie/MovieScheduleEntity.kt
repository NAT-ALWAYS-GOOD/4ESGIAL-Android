package com.nat.cineandroid.data.movie

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "movie_schedule",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("movieId")]
)
data class MovieScheduleEntity(
    @PrimaryKey val id: Int,
    val startTime: Instant,
    val endTime: Instant,
    val movieId: Int
)
