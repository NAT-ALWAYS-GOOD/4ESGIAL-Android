package com.nat.cineandroid.data.session

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nat.cineandroid.core.cache.ApplicationCache
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.session.dao.SessionDAO
import com.nat.cineandroid.data.session.entity.ReservationEntity
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.data.session.entity.SessionEntity
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class SessionDAOTest {
    private lateinit var database: ApplicationCache

    private lateinit var sessionDAO: SessionDAO
    private lateinit var cinemaRoomDAO: CinemaRoomDAO
    private lateinit var theaterDAO: TheaterDAO
    private lateinit var movieDAO: MovieDAO

    @Before
    fun setUp() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApplicationCache::class.java
        )
            .allowMainThreadQueries()
            .build()

        sessionDAO = database.sessionDao()
        cinemaRoomDAO = database.cinemaRoomDao()
        theaterDAO = database.theaterDao()
        movieDAO = database.movieDao()

        val theater100 = TheaterEntity(
            id = 100,
            name = "Theater Test",
            description = "Test description",
            streetName = "Test Street",
            streetNumber = "1",
            postalCode = "75000",
            city = "Paris",
            country = "France",
            latitude = 48.8566,
            longitude = 2.3522
        )
        val theater200 = TheaterEntity(
            id = 200,
            name = "Theater Test 200",
            description = "Test description 200",
            streetName = "Another Street",
            streetNumber = "2",
            postalCode = "75001",
            city = "Paris",
            country = "France",
            latitude = 48.8570,
            longitude = 2.3530
        )
        theaterDAO.upsertTheaters(listOf(theater100, theater200))

        val room1 = CinemaRoomEntity(
            id = 1,
            theaterId = 100,
            name = "Salle A",
            capacity = 50,
            description = "Salle A description",
            type = "Standard",
            isAccessible = true
        )
        val room2 = CinemaRoomEntity(
            id = 2,
            theaterId = 200,
            name = "Salle B",
            capacity = 75,
            description = "Salle B description",
            type = "IMAX",
            isAccessible = false
        )
        cinemaRoomDAO.upsertRooms(listOf(room1, room2))
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertSessions_and_getSessions_should_work() = runTest {
        val now = Instant.now()

        val movie1 = MovieEntity(
            id = 10,
            title = "Movie 10",
            description = "Description of Movie 10",
            duration = 120,
            releaseDate = now.minus(10, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer10",
            imageUrl = "http://example.com/movie10.jpg"
        )
        movieDAO.upsertMovies(listOf(movie1))

        val session1 = SessionEntity(
            id = 1,
            movieId = 10,
            roomId = 1,
            startTime = now.plus(1, ChronoUnit.DAYS),
            endTime = now.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
        )

        val movie2 = MovieEntity(
            id = 20,
            title = "Movie 20",
            description = "Description of Movie 20",
            duration = 100,
            releaseDate = now.minus(5, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer20",
            imageUrl = "http://example.com/movie20.jpg"
        )
        movieDAO.upsertMovies(listOf(movie2))

        val session2 = SessionEntity(
            id = 2,
            movieId = 20,
            roomId = 2,
            startTime = now.plus(2, ChronoUnit.DAYS),
            endTime = now.plus(2, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
        )
        sessionDAO.upsertSessions(listOf(session1, session2))
        val sessions = sessionDAO.getSessions()
        assertThat(sessions).hasSize(2)
    }

    @Test
    fun getSessionsWithSeatsFromNowToNextWeek_should_return_correct_sessions() = runTest {
        val now = Instant.now()
        val nextWeek = now.plus(7, ChronoUnit.DAYS)
        val movie = MovieEntity(
            id = 30,
            title = "Movie 30",
            description = "Description of Movie 30",
            duration = 110,
            releaseDate = now.minus(3, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer30",
            imageUrl = "http://example.com/movie30.jpg"
        )
        movieDAO.upsertMovies(listOf(movie))

        val session = SessionEntity(
            id = 3,
            movieId = 30,
            roomId = 1,
            startTime = now.plus(3, ChronoUnit.DAYS),
            endTime = now.plus(3, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
        )
        sessionDAO.upsertSessions(listOf(session))
        val seat1 = SeatEntity(
            sessionId = 3,
            seatNumber = 1,
            isReserved = false
        )
        val seat2 = SeatEntity(
            sessionId = 3,
            seatNumber = 2,
            isReserved = true
        )
        sessionDAO.upsertSeats(listOf(seat1, seat2))

        val result = sessionDAO.getSessionsWithSeatsFromNowToNextWeek(
            movieId = 30,
            theaterId = 100,
            today = now,
            nextWeek = nextWeek
        )
        assertThat(result).hasSize(1)
        val sessionWithSeats = result[0]
        assertThat(sessionWithSeats.session.id).isEqualTo(3)
        assertThat(sessionWithSeats.seats).hasSize(2)
    }

    @Test
    fun getUserReservations_should_return_reservations_for_user() = runTest {
        val now = Instant.now()
        val movie = MovieEntity(
            id = 40,
            title = "Movie 40",
            description = "Description of Movie 40",
            duration = 95,
            releaseDate = now.minus(7, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer40",
            imageUrl = "http://example.com/movie40.jpg"
        )
        movieDAO.upsertMovies(listOf(movie))
        val session = SessionEntity(
            id = 4,
            movieId = 40,
            roomId = 1,
            startTime = now.plus(1, ChronoUnit.DAYS),
            endTime = now.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
        )
        sessionDAO.upsertSessions(listOf(session))

        val reservation = ReservationEntity(
            reference = "AZERTY12345",
            createdAt = Instant.now(),
            qrCode = "QRCODE12345",
            seats = listOf(1, 2),
            userId = 1000,
            sessionId = 4
        )
        sessionDAO.upsertReservation(reservation)
        val reservations = sessionDAO.getUserReservations(1000)
        assertThat(reservations).hasSize(1)
        assertThat(reservations[0].reference).isEqualTo("AZERTY12345")
    }

    @Test
    fun getSessionIds_should_return_correct_ids() = runTest {
        val now = Instant.now()
        val selectedDate = now.plus(1, ChronoUnit.DAYS).toString().substring(0, 10)

        val movie = MovieEntity(
            id = 50,
            title = "Movie 50",
            description = "Description of Movie 50",
            duration = 105,
            releaseDate = now.minus(2, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer50",
            imageUrl = "http://example.com/movie50.jpg"
        )
        movieDAO.upsertMovies(listOf(movie))

        val session = SessionEntity(
            id = 5,
            movieId = 50,
            roomId = 1,
            startTime = Instant.parse("${selectedDate}T10:00:00Z"),
            endTime = Instant.parse("${selectedDate}T12:00:00Z")
        )
        sessionDAO.upsertSessions(listOf(session))
        val ids = sessionDAO.getSessionIds(movieId = 50, theaterId = 100, selectedDate = selectedDate)
        assertThat(ids).containsExactly(5)
    }

    @Test
    fun getSessionById_should_return_correct_session() = runTest {
        val now = Instant.now()

        val movie = MovieEntity(
            id = 60,
            title = "Movie 60",
            description = "Description of Movie 60",
            duration = 110,
            releaseDate = now.minus(3, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer60",
            imageUrl = "http://example.com/movie60.jpg"
        )
        movieDAO.upsertMovies(listOf(movie))

        val session = SessionEntity(
            id = 6,
            movieId = 60,
            roomId = 2,
            startTime = now.plus(4, ChronoUnit.DAYS),
            endTime = now.plus(4, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
        )
        sessionDAO.upsertSessions(listOf(session))
        val retrieved = sessionDAO.getSessionById(6)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.id).isEqualTo(6)
    }
}