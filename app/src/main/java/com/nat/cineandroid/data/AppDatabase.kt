package com.nat.cineandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nat.cineandroid.data.model.ReservationEntity
import com.nat.cineandroid.data.model.UserDAO
import com.nat.cineandroid.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ReservationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}