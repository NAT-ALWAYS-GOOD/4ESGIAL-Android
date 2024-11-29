package com.nat.cineandroid.core.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nat.cineandroid.data.cinemaRoom.CinemaRoomEntity
import com.nat.cineandroid.data.movie.MovieEntity
import com.nat.cineandroid.data.movie.MovieScheduleEntity
import com.nat.cineandroid.data.session.ReservationEntity
import com.nat.cineandroid.data.session.SeatEntity
import com.nat.cineandroid.data.session.SessionEntity
import com.nat.cineandroid.data.theater.TheaterEntity
import com.nat.cineandroid.data.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TheaterEntity::class,
        SessionEntity::class,
        SeatEntity::class,
        ReservationEntity::class,
        MovieScheduleEntity::class,
        MovieEntity::class,
        CinemaRoomEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ApplicationCache : RoomDatabase() {
//    abstract fun userDao(): UserDAO
}