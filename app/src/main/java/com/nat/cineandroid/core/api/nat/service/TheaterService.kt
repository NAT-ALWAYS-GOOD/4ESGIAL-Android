package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseWithSessionsDTO
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TheaterService {
    @GET("theaters/{theaterId}/movies-sessions")
    suspend fun getMoviesByTheaterId(@Path("theaterId") theaterId: Int): Response<List<MovieResponseWithSessionsDTO>>

    @GET("theaters")
    suspend fun getTheaters(): Response<List<TheaterResponseDTO>>
}