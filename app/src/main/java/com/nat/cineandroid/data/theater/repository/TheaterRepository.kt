package com.nat.cineandroid.data.theater.repository

import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheaterRepository @Inject constructor(
    private val theaterDAO: TheaterDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
)