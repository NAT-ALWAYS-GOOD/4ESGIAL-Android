package com.nat.cineandroid.data.theater.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nat.cineandroid.data.theater.entity.TheaterEntity

@Dao
interface TheaterDAO {
    @Upsert
    suspend fun upsertTheaters(theaters: List<TheaterEntity>)

    @Query("SELECT * FROM theater")
    suspend fun getTheaters(): List<TheaterEntity>

    @Query("SELECT * FROM theater WHERE id = :theaterId")
    suspend fun getTheaterById(theaterId: Int): TheaterEntity
}