package com.nat.cineandroid.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservations(reservations: List<ReservationEntity>)

    @Transaction
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUser(userId: Int): UserWithReservations?

    @Delete
    suspend fun deleteUser(user: UserEntity)
}