package com.nat.cineandroid.data.movie

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nat.cineandroid.core.cache.ApplicationCache
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.movie.entity.MovieWithSessions
import com.nat.cineandroid.data.session.dao.SessionDAO
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
class MovieDAOTest {

    private lateinit var database: ApplicationCache
    private lateinit var movieDAO: MovieDAO
    private lateinit var sessionDAO: SessionDAO
    private lateinit var cinemaRoomDAO: CinemaRoomDAO
    private lateinit var theaterDAO: TheaterDAO

    @Before
    fun setUp() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApplicationCache::class.java
        )
            .allowMainThreadQueries()
            .build()

        movieDAO = database.movieDao()
        sessionDAO = database.sessionDao()
        cinemaRoomDAO = database.cinemaRoomDao()
        theaterDAO = database.theaterDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertMovie_and_getMovieById_should_work() = runTest {
        val now = Instant.now()
        val movie = MovieEntity(
            id = 1,
            title = "Test Movie",
            description = "A test movie",
            duration = 120,
            releaseDate = now.minus(1, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "abc123",
            imageUrl = "http://example.com/movie.jpg"
        )
        movieDAO.upsertMovie(movie)
        val retrieved = movieDAO.getMovieById(1)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.title).isEqualTo("Test Movie")
    }

    @Test
    fun upsertMovies_and_getMovies_should_work() = runTest {
        val now = Instant.now()
        val movie1 = MovieEntity(
            id = 1,
            title = "Movie 1",
            description = "Description 1",
            duration = 100,
            releaseDate = now.minus(2, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer1",
            imageUrl = "http://example.com/movie1.jpg"
        )
        val movie2 = MovieEntity(
            id = 2,
            title = "Movie 2",
            description = "Description 2",
            duration = 110,
            releaseDate = now.minus(3, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailer2",
            imageUrl = "http://example.com/movie2.jpg"
        )
        movieDAO.upsertMovies(listOf(movie1, movie2))
        val movies = movieDAO.getMovies()
        assertThat(movies).hasSize(2)
        assertThat(movies.map { it.id }).containsExactlyInAnyOrder(1, 2)
    }

    @Test
    fun getNotReleasedMovies_should_return_future_movies() = runTest {
        val now = Instant.now()
        val pastMovie = MovieEntity(
            id = 1,
            title = "Past Movie",
            description = "Already released",
            duration = 90,
            releaseDate = now.minus(1, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailerPast",
            imageUrl = "http://example.com/past.jpg"
        )
        val futureMovie = MovieEntity(
            id = 2,
            title = "Future Movie",
            description = "Not released yet",
            duration = 100,
            releaseDate = now.plus(1, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailerFuture",
            imageUrl = "http://example.com/future.jpg"
        )
        movieDAO.upsertMovies(listOf(pastMovie, futureMovie))
        val notReleased = movieDAO.getNotReleasedMovies()
        assertThat(notReleased).hasSize(1)
        assertThat(notReleased[0].id).isEqualTo(2)
    }

    @Test
    fun getMovieById_should_return_correct_movie() = runTest {
        val now = Instant.now()
        val movie = MovieEntity(
            id = 3,
            title = "Specific Movie",
            description = "Specific description",
            duration = 105,
            releaseDate = now.minus(1, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailerSpecific",
            imageUrl = "http://example.com/specific.jpg"
        )
        movieDAO.upsertMovie(movie)
        val retrieved = movieDAO.getMovieById(3)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.title).isEqualTo("Specific Movie")
    }

    @Test
    fun getMoviesByTheaterId_should_return_movies_with_sessions() = runTest {
        val now = Instant.now()

        val theater = TheaterEntity(
            id = 100,
            name = "Test Theater",
            description = "Test Theater Description",
            streetName = "Test Street",
            streetNumber = "1",
            postalCode = "12345",
            city = "TestCity",
            country = "TestCountry",
            latitude = 0.0,
            longitude = 0.0
        )
        theaterDAO.upsertTheaters(listOf(theater))

        val movie = MovieEntity(
            id = 10,
            title = "Theater Movie",
            description = "Movie associated with a theater",
            duration = 120,
            releaseDate = now.minus(2, ChronoUnit.DAYS),
            isActive = true,
            trailerYoutubeId = "trailerTheater",
            imageUrl = "http://example.com/theaterMovie.jpg"
        )
        movieDAO.upsertMovie(movie)

        val room = CinemaRoomEntity(
            id = 1,
            theaterId = 100,
            name = "Room A",
            capacity = 50,
            description = "Room A description",
            type = "Standard",
            isAccessible = true
        )
        cinemaRoomDAO.upsertRooms(listOf(room))

        val session = SessionEntity(
            id = 1,
            movieId = 10,
            roomId = 1,
            startTime = now.plus(1, ChronoUnit.DAYS),
            endTime = now.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS)
        )
        sessionDAO.upsertSessions(listOf(session))

        val moviesWithSessions: List<MovieWithSessions> = movieDAO.getMoviesByTheaterId(100)
        assertThat(moviesWithSessions).hasSize(1)
        val retrievedMovie = moviesWithSessions[0].movie
        assertThat(retrievedMovie.id).isEqualTo(10)
        assertThat(moviesWithSessions[0].sessions).hasSize(1)
        assertThat(moviesWithSessions[0].sessions[0].id).isEqualTo(1)
    }
}