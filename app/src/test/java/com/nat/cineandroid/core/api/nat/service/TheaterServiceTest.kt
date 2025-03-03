package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseWithSessionsDTO
import com.nat.cineandroid.core.api.nat.dto.session.SessionPartialResponseWithoutMovieDTO
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response

class TheaterServiceTest {
    @Mock
    private lateinit var theaterService: TheaterService

    @Before
    fun setUp() {
        theaterService = Mockito.mock(TheaterService::class.java)
    }

    @Test
    fun `should get movies by theater id successfully`() = runTest {
        val fakeMoviesSessions = listOf(
            MovieResponseWithSessionsDTO(
                movie = MovieResponseDTO(
                    id = 1,
                    title = "Inception",
                    description = "A mind-bending thriller",
                    duration = 148,
                    releaseDate = "2010-07-16T00:00:00Z",
                    isActive = true,
                    trailerYoutubeId = "YoHD9XEInc0",
                    imageUrl = "https://example.com/inception.jpg"
                ),
                sessions = listOf(
                    SessionPartialResponseWithoutMovieDTO(
                        id = 1,
                        startTime = "2025-03-15T19:30:00Z",
                        endTime = "2025-03-15T22:00:00Z",
                        room = CinemaRoomPartialResponseDTO(
                            id = 201,
                            name = "Main Hall",
                            description = "The largest cinema room with state-of-the-art sound",
                            type = "IMAX",
                            capacity = 250,
                            accessibility = true
                        )
                    )
                )
            )
        )

        `when`(theaterService.getMoviesByTheaterId(1))
            .thenReturn(Response.success(fakeMoviesSessions))

        val response = theaterService.getMoviesByTheaterId(1)
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body).hasSize(1)
        assertThat(body!![0].movie.title).isEqualTo("Inception")
    }

    @Test
    fun `should get theaters successfully`() = runTest {
        val fakeTheaters = listOf(
            TheaterResponseDTO(
                id = 1,
                name = "Grand Cinema",
                description = "Top-notch theater",
                streetName = "Main Street",
                streetNumber = "123",
                postalCode = "12345",
                city = "Metropolis",
                country = "Wonderland",
                latitude = 40.7128,
                longitude = -74.0060
            )
        )

        `when`(theaterService.getTheaters()).thenReturn(Response.success(fakeTheaters))

        val response = theaterService.getTheaters()
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body).hasSize(1)
        assertThat(body!![0].name).isEqualTo("Grand Cinema")
    }

    @Test
    fun `should update favorite theater successfully`() = runTest {
        // Pour updateFavoriteTheater, on simule un succès renvoyant Response.success(Unit)
        `when`(theaterService.updateFavoriteTheater(1, 42))
            .thenReturn(Response.success(Unit))

        val response = theaterService.updateFavoriteTheater(1, 42)
        assertThat(response.isSuccessful).isTrue
        // Response.body() devrait être null pour un Response<Unit> réussi
        assertThat(response.code()).isEqualTo(200)
    }
}