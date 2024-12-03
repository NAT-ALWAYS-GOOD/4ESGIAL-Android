package com.nat.cineandroid.data.cinemaRoom.repository

import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CinemaRoomRepository @Inject constructor(
    private val cinemaRoomDAO: CinemaRoomDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
)