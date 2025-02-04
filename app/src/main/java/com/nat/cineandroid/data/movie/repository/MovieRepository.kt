package com.nat.cineandroid.data.movie.repository

import android.util.Log
import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseWithSessionsDTO
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.movie.entity.MovieWithSessions
import com.nat.cineandroid.data.session.dao.SessionDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieDAO: MovieDAO,
    private val sessionDAO: SessionDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
) {
    suspend fun getMoviesByTheater(theaterId: Int): HttpResult<List<MovieWithSessions>> {
        return httpClient.fetchData(
            networkCall = {
                val response = apiService.getMoviesByTheaterId(theaterId)
                Log.d("Repository", "API response: ${response.body()?.toString()}")
                response
            },
            cacheCall = {
                val cachedMovies = movieDAO.getMoviesByTheaterId(theaterId)
                Log.d("Repository", "Cache response: $cachedMovies")
                cachedMovies
            },
            saveToCache = { it.forEach { movieWithSessions ->
                    movieDAO.upsertMovie(movieWithSessions.movie)
                    sessionDAO.upsertSessions(movieWithSessions.sessions)
                    Log.d("Repository", "Saved to cache: $movieWithSessions")
                }
                Log.d("Repository", "Saved to cache: $it")
            },
            transformResponse = { dtoList: List<MovieResponseWithSessionsDTO> ->
                dtoList.map { it.toEntity() }
                    .also { Log.d("Repository", "Transformed response: $it") }
            }
        )
    }
}