package com.nat.cineandroid.data.movie.repository

import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.data.movie.dao.MovieDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieDAO: MovieDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
)