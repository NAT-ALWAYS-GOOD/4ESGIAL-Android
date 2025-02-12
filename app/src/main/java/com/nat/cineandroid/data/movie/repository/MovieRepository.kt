package com.nat.cineandroid.data.movie.repository

import android.util.Log
import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseWithSessionsDTO
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.movie.entity.FullMoviesData
import com.nat.cineandroid.data.movie.entity.MovieWithSessions
import com.nat.cineandroid.data.session.dao.SessionDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieDAO: MovieDAO,
    private val sessionDAO: SessionDAO,
    private val cinemaRoomDAO: CinemaRoomDAO,
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
                val cachedCinemaRooms = cinemaRoomDAO.getCinemaRoomsByTheaterId(theaterId)
                FullMoviesData(
                    moviesWithSessions = cachedMovies,
                    cinemaRooms = cachedCinemaRooms
                )
            },
            saveToCache = { fullData: FullMoviesData ->
                cinemaRoomDAO.upsertRooms(fullData.cinemaRooms)
                fullData.moviesWithSessions.forEach { movieWithSessions ->
                    movieDAO.upsertMovie(movieWithSessions.movie)
                    sessionDAO.upsertSessions(movieWithSessions.sessions)
                }
                Log.d("Repository", "Saved movies: ${movieDAO.getMovies()}")
                Log.d("Repository", "Saved sessions: ${sessionDAO.getSessions()}")
                Log.d("Repository", "Saved cinema rooms: ${cinemaRoomDAO.getCinemaRoomsByTheaterId(theaterId)}")
            },
            transformResponse = { dtoList: List<MovieResponseWithSessionsDTO> ->
                val moviesWithSessions = dtoList.map { it.toEntity() }
                val cinemaRooms =
                    dtoList.flatMap { it.sessions.map { it.room.toCinemaRoomEntity(theaterId) } }
                        .distinctBy { it.id }
                FullMoviesData(
                    moviesWithSessions,
                    cinemaRooms
                )
            }
        ).let { result ->
            when (result) {
                is HttpResult.Success -> HttpResult.Success(result.data.moviesWithSessions)
                is HttpResult.HttpError -> HttpResult.HttpError(result.code, result.message)
                is HttpResult.NetworkError -> HttpResult.NetworkError(result.message)
                is HttpResult.NoData -> HttpResult.NoData(result.message)
            }
        }
    }
}