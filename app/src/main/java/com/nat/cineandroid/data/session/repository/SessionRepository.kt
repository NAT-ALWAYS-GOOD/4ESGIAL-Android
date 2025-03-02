package com.nat.cineandroid.data.session.repository

import android.util.Log
import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.api.nat.dto.session.CreateResesrvationRequestDTO
import com.nat.cineandroid.core.api.nat.dto.session.ReservationResponseDTO
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.session.dao.SessionDAO
import com.nat.cineandroid.data.session.entity.FullReservationsData
import com.nat.cineandroid.data.session.entity.ReservationEntity
import com.nat.cineandroid.data.session.entity.SessionEntity
import com.nat.cineandroid.data.session.entity.SessionWithSeats
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDAO: SessionDAO,
    private val theaterDAO: TheaterDAO,
    private val movieDAO: MovieDAO,
    private val cinemaRoomDAO: CinemaRoomDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
) {
    suspend fun getUserReservations(userId: Int): HttpResult<FullReservationsData> {
        return httpClient.fetchData(
            networkCall = {
                apiService.getUserReservations(userId)
            },
            cacheCall = {
                Log.d("ProfileFragment", "Cache response")
                val reservations = sessionDAO.getUserReservations(userId)
                val theaters = theaterDAO.getTheaters()
                val sessions = sessionDAO.getSessions()
                val movies = movieDAO.getMovies()
                val cinemaRoomsId = sessions.map { it.roomId }
                val cinemaRooms = cinemaRoomDAO.getCinemaRoomsByIds(cinemaRoomsId)
                FullReservationsData(
                    reservations,
                    theaters,
                    sessions,
                    movies,
                    cinemaRooms
                )
            },
            saveToCache = { fullReservationsData: FullReservationsData ->
                theaterDAO.upsertTheaters(fullReservationsData.theaters)
                movieDAO.upsertMovies(fullReservationsData.movies)
                cinemaRoomDAO.upsertRooms(fullReservationsData.cinemaRooms)
                sessionDAO.upsertSessions(fullReservationsData.sessions)
                sessionDAO.upsertReservations(fullReservationsData.reservations)
            },
            transformResponse = { dtoList: List<ReservationResponseDTO> ->
                val theaters = dtoList.map { it.theaterEntity }
                val sessions = dtoList.map { it.session.toSessionEntity() }
                val movies = dtoList.map { it.session.movie.toMovieEntity() }
                val cinemaRooms =
                    dtoList.map { it.session.room.toCinemaRoomEntity(it.theaterEntity.id) }
                val reservations = dtoList.map { it.toReservationEntity() }

                FullReservationsData(
                    reservations,
                    theaters,
                    sessions,
                    movies,
                    cinemaRooms
                )
            }
        )
    }

    suspend fun getAllSessionsFromMovieIdAndTheaterId(
        movieId: Int,
        theaterId: Int
    ): HttpResult<List<SessionWithSeats>> {
        val today = Instant.now()
        val weekLater = today.plus(7, ChronoUnit.DAYS)

        return httpClient.fetchData(
            networkCall = {
                apiService.getAllSessionsFromMovieIdAndTheaterId(movieId, theaterId)
                    .also { response ->
                        Log.d(
                            "SelectSessionFragment",
                            "API response: ${response.body()?.toString()}"
                        )
                    }
            },
            cacheCall = {
                Log.d("SelectSessionFragment", "Cache response")
                sessionDAO.getSessionsWithSeatsFromNowToNextWeek(
                    movieId,
                    theaterId,
                    today,
                    weekLater
                )
            },
            saveToCache = { sessionWithSeats: List<SessionWithSeats> ->
                sessionDAO.upsertSessions(sessionWithSeats.map { it.session })
                sessionDAO.upsertSeats(sessionWithSeats.flatMap { it.seats })
            },
            transformResponse = { dtoList ->
                dtoList.map { it.toSessionEntity() to it.toSeatEntities() }
                    .map { (session, seats) ->
                        SessionWithSeats(
                            session,
                            seats
                        )
                    }
            }
        )
    }

    suspend fun getSessionFromCache(sessionId: Int): SessionEntity {
        return sessionDAO.getSessionById(sessionId)
    }

    suspend fun reserveSeats(
        sessionId: Int,
        userId: Int,
        seatNumbers: List<Int>
    ): HttpResult<ReservationEntity> {
        return httpClient.createData(
            networkCall = {
                val createResesrvationRequestDTO = CreateResesrvationRequestDTO(
                    sessionId,
                    userId,
                    seatNumbers
                )
                apiService.reserveSeats(createResesrvationRequestDTO)
            },
            saveToCache = { reservation: ReservationEntity ->
                sessionDAO.upsertReservation(reservation)
            },
            transformResponse = { dto ->
                dto.toReservationEntity()
            }
        )
    }
}