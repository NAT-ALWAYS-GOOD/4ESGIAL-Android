package com.nat.cineandroid.data.movie.entity

import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity

data class FullMoviesData(
    val moviesWithSessions: List<MovieWithSessions>,
    val cinemaRooms: List<CinemaRoomEntity>
)
