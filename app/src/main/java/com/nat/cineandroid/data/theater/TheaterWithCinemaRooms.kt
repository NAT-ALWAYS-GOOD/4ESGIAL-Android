package com.nat.cineandroid.data.theater

import androidx.room.Embedded
import androidx.room.Relation
import com.nat.cineandroid.data.cinemaRoom.CinemaRoomEntity

data class TheaterWithCinemaRooms(
    @Embedded val theater: TheaterEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "theaterId"
    )
    val cinemaRooms: List<CinemaRoomEntity>
)
