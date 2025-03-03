package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import com.nat.cineandroid.core.api.nat.dto.session.CreateResesrvationRequestDTO
import com.nat.cineandroid.core.api.nat.dto.session.ReservationResponseDTO
import com.nat.cineandroid.core.api.nat.dto.session.SeatResponseDTO
import com.nat.cineandroid.core.api.nat.dto.session.SessionPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.session.SessionResponseDTO
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response

class SessionServiceTest {
    @Mock
    private lateinit var sessionService: SessionService

    @Before
    fun setUp() {
        sessionService = Mockito.mock(SessionService::class.java)
    }

    @Test
    fun `should get user reservations successfully`() = runTest {
        val fakeReservations = listOf(
            ReservationResponseDTO(
                reference = "AZERTY12345",
                createdAt = "2025-06-01T12:00:00Z",
                qrCode = "QRCODE12345",
                seats = listOf(1, 2),
                userId = 1,
                session = SessionPartialResponseDTO(
                    id = 10,
                    startTime = "2025-08-01T20:00:00Z",
                    endTime = "2025-08-01T22:00:00Z",
                    movie = MovieResponseDTO(
                        id = 1,
                        title = "title",
                        description = "description",
                        duration = 120,
                        releaseDate = "2021-01-01T00:00:00Z",
                        isActive = true,
                        trailerYoutubeId = "trailerYoutubeId",
                        imageUrl = "https://image.url"
                    ),
                    room = CinemaRoomPartialResponseDTO(
                        id = 1,
                        name = "Salle 1",
                        description = "Description Salle 1",
                        type = "Standard",
                        capacity = 45,
                        accessibility = true
                    )
                ),
                theaterEntity = TheaterEntity(
                    id = 10,
                    name = "Theater 1",
                    description = "Description Theater 1",
                    streetName = "Street",
                    streetNumber = "123",
                    postalCode = "75001",
                    city = "Paris",
                    country = "France",
                    latitude = 48.8566,
                    longitude = 2.3522
                )
            )
        )

        `when`(sessionService.getUserReservations(1)).thenReturn(Response.success(fakeReservations))

        val response = sessionService.getUserReservations(1)
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body).hasSize(1)
        assertThat(body!![0].reference).isEqualTo("AZERTY12345")
    }

    // Test pour getAllSessionsFromMovieIdAndTheaterId
    @Test
    fun `should get sessions for movie and theater successfully`() = runTest {
        val fakeSessions = listOf(
            SessionResponseDTO(
                id = 1,
                movie = MovieResponseDTO(
                    id = 101,
                    title = "The Matrix",
                    description = "A groundbreaking sci-fi film",
                    duration = 136,
                    releaseDate = "1999-03-31T00:00:00Z",
                    isActive = true,
                    trailerYoutubeId = "abc123XYZ",
                    imageUrl = "https://example.com/matrix.jpg"
                ),
                room = CinemaRoomResponseDTO(
                    id = 201,
                    name = "Main Hall",
                    description = "The largest cinema room with state-of-the-art sound",
                    type = "IMAX",
                    capacity = 250,
                    accessibility = true,
                    theater = TheaterResponseDTO(
                        id = 301,
                        name = "Grand Cinema",
                        description = "A premium movie theater in downtown",
                        streetName = "Broadway",
                        streetNumber = "123",
                        postalCode = "10001",
                        city = "New York",
                        country = "USA",
                        latitude = 40.7128,
                        longitude = -74.0060
                    )
                ),
                startTime = "2025-03-15T19:30:00Z",
                endTime = "2025-03-15T22:00:00Z",
                seats = listOf(
                    SeatResponseDTO(
                        seatNumber = 1,
                        isReserved = false
                    ),
                    SeatResponseDTO(
                        seatNumber = 2,
                        isReserved = true
                    )
                )
            )
        )

        `when`(sessionService.getAllSessionsFromMovieIdAndTheaterId(2, 5))
            .thenReturn(Response.success(fakeSessions))

        val response = sessionService.getAllSessionsFromMovieIdAndTheaterId(2, 5)
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body).hasSize(1)
        assertThat(body!![0].id).isEqualTo(1)
    }

    // Test pour reserveSeats en cas de succès
    @Test
    fun `should reserve seats successfully`() = runTest {
        val request = CreateResesrvationRequestDTO(
            sessionId = 10,
            userId = 1,
            seats = listOf(1, 2)
        )
        val fakeReservation = ReservationResponseDTO(
            reference = "AZERTY12345",
            createdAt = "2025-06-01T12:00:00Z",
            qrCode = "QRCODE12345",
            seats = listOf(1, 2),
            userId = 1,
            session = SessionPartialResponseDTO(
                id = 10,
                startTime = "2025-08-01T20:00:00Z",
                endTime = "2025-08-01T22:00:00Z",
                movie = MovieResponseDTO(
                    id = 1,
                    title = "title",
                    description = "description",
                    duration = 120,
                    releaseDate = "2021-01-01T00:00:00Z",
                    isActive = true,
                    trailerYoutubeId = "trailerYoutubeId",
                    imageUrl = "https://image.url"
                ),
                room = CinemaRoomPartialResponseDTO(
                    id = 1,
                    name = "Salle 1",
                    description = "Description Salle 1",
                    type = "Standard",
                    capacity = 45,
                    accessibility = true
                )
            ),
            theaterEntity = TheaterEntity(
                id = 10,
                name = "Theater 1",
                description = "Description Theater 1",
                streetName = "Street",
                streetNumber = "123",
                postalCode = "75001",
                city = "Paris",
                country = "France",
                latitude = 48.8566,
                longitude = 2.3522
            )
        )

        `when`(sessionService.reserveSeats(request)).thenReturn(Response.success(fakeReservation))

        val response = sessionService.reserveSeats(request)
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body!!.reference).isEqualTo("AZERTY12345")
        assertThat(body.seats).isEqualTo(listOf(1, 2))
    }
}