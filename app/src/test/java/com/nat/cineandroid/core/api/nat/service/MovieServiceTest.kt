package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response


class MovieServiceTest {
    @Mock
    private lateinit var movieService: MovieService

    @Before
    fun setUp() {
        movieService = Mockito.mock(MovieService::class.java)
    }

    @Test
    fun `should get movies`() = runTest {
        val movies = listOf(
            MovieResponseDTO(
                id = 1,
                title = "title",
                description = "description",
                duration = 120,
                releaseDate = "2021-01-01T00:00:00Z",
                isActive = true,
                trailerYoutubeId = "trailerYoutubeId",
                imageUrl = "https://image.url"
            )
        )

        `when`(movieService.getMovies(true)).thenReturn(Response.success(movies))

        val response = movieService.getMovies(true)
        assertThat(response.isSuccessful).isTrue

        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body).hasSize(1)
    }
}