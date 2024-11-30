package com.nat.cineandroid.core.api.nat

import com.nat.cineandroid.core.api.nat.service.CinemaRoomService
import com.nat.cineandroid.core.api.nat.service.MovieService
import com.nat.cineandroid.core.api.nat.service.SessionService
import com.nat.cineandroid.core.api.nat.service.TheaterService
import com.nat.cineandroid.core.api.nat.service.UserService

interface NATCinemasAPI :
    UserService,
    MovieService,
    CinemaRoomService,
    TheaterService,
    SessionService {
}