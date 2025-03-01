package com.nat.cineandroid.data.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nat.cineandroid.data.user.entity.UserEntity

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUser(userId: Int): UserEntity

    @Update
    suspend fun updateUser(user: UserEntity)
}