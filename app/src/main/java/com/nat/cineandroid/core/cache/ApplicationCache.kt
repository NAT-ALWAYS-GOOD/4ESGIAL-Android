package com.nat.cineandroid.core.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.movie.entity.MovieScheduleEntity
import com.nat.cineandroid.data.session.dao.SessionDAO
import com.nat.cineandroid.data.session.entity.ReservationEntity
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.data.session.entity.SessionEntity
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import com.nat.cineandroid.data.user.dao.UserDAO
import com.nat.cineandroid.data.user.entity.UserEntity

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
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ApplicationCache : RoomDatabase() {
    abstract fun cinemaRoomDao(): CinemaRoomDAO
    abstract fun movieDao(): MovieDAO
    abstract fun sessionDao(): SessionDAO
    abstract fun theaterDao(): TheaterDAO
    abstract fun userDao(): UserDAO
}