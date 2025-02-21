package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movies")
    suspend fun getMovies(@Query("released") released: Boolean? = null): Response<List<MovieResponseDTO>>
}