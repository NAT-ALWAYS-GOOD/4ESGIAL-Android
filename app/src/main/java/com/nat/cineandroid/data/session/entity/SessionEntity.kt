package com.nat.cineandroid.data.session.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.movie.entity.MovieScheduleEntity
import java.time.Instant

@Entity(
    tableName = "session",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
//        ForeignKey(
//            entity = CinemaRoomEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["roomId"],
//            onDelete = ForeignKey.CASCADE
//        )
    ],
    indices = [Index("movieId"), Index("roomId")]
)
data class SessionEntity(
    @PrimaryKey val id: Int,
    val startTime: Instant,
    val endTime: Instant,
    val movieId: Int,
    val roomId: Int
)
