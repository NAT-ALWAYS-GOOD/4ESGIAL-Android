package com.nat.cineandroid.data.session.entity

import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.theater.entity.TheaterEntity

data class FullReservationsData(
    val reservations: List<ReservationEntity>,
    val theaters: List<TheaterEntity>,
    val sessions: List<SessionEntity>,
    val movies: List<MovieEntity>,
    val cinemaRooms: List<CinemaRoomEntity>
)
