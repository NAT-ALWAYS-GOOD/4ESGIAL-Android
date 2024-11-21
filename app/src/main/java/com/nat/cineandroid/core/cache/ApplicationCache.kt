package com.nat.cineandroid.core.cache

import androidx.room.Database
import androidx.room.RoomDatabase

/*@Database(
    entities = [],
    version = 0,
    exportSchema = false
)*/
abstract class ApplicationCache : RoomDatabase() {
//    abstract fun userDao(): UserDAO
}