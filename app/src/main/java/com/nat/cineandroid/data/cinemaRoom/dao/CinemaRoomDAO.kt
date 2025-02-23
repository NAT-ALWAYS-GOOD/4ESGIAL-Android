package com.nat.cineandroid.data.cinemaRoom.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity

@Dao
interface CinemaRoomDAO {
    @Upsert
    suspend fun upsertRooms(rooms: List<CinemaRoomEntity>)

    @Query("SELECT * FROM cinema_room WHERE theaterId = :theaterId")
    suspend fun getCinemaRoomsByTheaterId(theaterId: Int): List<CinemaRoomEntity>

    @Query("SELECT * FROM cinema_room WHERE id IN (:cinemaRoomIds)")
    suspend fun getCinemaRoomsByIds(cinemaRoomIds: List<Int>): List<CinemaRoomEntity>
}
