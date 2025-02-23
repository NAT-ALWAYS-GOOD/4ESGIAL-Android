package com.nat.cineandroid.data.session.entity

import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.theater.entity.TheaterEntity

data class ReservationItemData(
    val reservation: ReservationEntity,
    val session: SessionEntity,
    val movie: MovieEntity,
    val cinemaRoom: CinemaRoomEntity,
    val theater: TheaterEntity
)