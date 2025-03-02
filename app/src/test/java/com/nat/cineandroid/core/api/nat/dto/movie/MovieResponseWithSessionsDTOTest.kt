package com.nat.cineandroid.core.api.nat.dto.movie

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.session.SessionPartialResponseWithoutMovieDTO
import com.nat.cineandroid.core.api.nat.dto.session.SessionResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MovieResponseWithSessionsDTOTest {
    private val gson = Gson()

    private val movieId = 1
    private val title = "Inception"
    private val movieDescription = "A mind-bending thriller"
    private val duration = 148
    private val releaseDate = "2025-07-16T00:00:00Z"
    private val isActive = true
    private val trailerYoutubeId = "YoHD9XEInc0"
    private val imageUrl = "https://example.com/inception.jpg"

    private val sessionId = 100
    private val sessionStartTime = "2020-01-01T10:00:00Z"
    private val sessionEndTime = "2020-01-01T12:00:00Z"

    private val roomId = 1
    private val name = "Salle 1"
    private val roomDescription = "Description Salle 1"
    private val type = "Standard"
    private val capacity = 100
    private val accessibility = true

    private val jsonInput = """
        {
            "movie": {
                "id": $movieId,
                "title": "$title",
                "description": "$movieDescription",
                "duration": $duration,
                "releaseDate": "$releaseDate",
                "isActive": $isActive,
                "trailerYoutubeId": "$trailerYoutubeId",
                "imageUrl": "$imageUrl"
            },
            "sessions": [
                {
                    "id": $sessionId,
                    "startTime": "$sessionStartTime",
                    "endTime": "$sessionEndTime",
                    "room": {
                        "id": $roomId,
                        "name": "$name",
                        "description": "$roomDescription",
                        "type": "$type",
                        "capacity": $capacity,
                        "accessibility": $accessibility
                    }
                }
            ]
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to MovieResponseWithSessionsDTO correctly`() {
        val dto = gson.fromJson(jsonInput, MovieResponseWithSessionsDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.movie.id).isEqualTo(movieId)
        assertThat(dto.movie.title).isEqualTo(title)
        assertThat(dto.movie.description).isEqualTo(movieDescription)
        assertThat(dto.movie.duration).isEqualTo(duration)
        assertThat(dto.movie.releaseDate).isEqualTo(releaseDate)
        assertThat(dto.movie.isActive).isEqualTo(isActive)
        assertThat(dto.movie.trailerYoutubeId).isEqualTo(trailerYoutubeId)
        assertThat(dto.movie.imageUrl).isEqualTo(imageUrl)
        assertThat(dto.sessions).hasSize(1)
        assertThat(dto.sessions.first().id).isEqualTo(sessionId)
        assertThat(dto.sessions.first().startTime).isEqualTo(sessionStartTime)
        assertThat(dto.sessions.first().endTime).isEqualTo(sessionEndTime)
        assertThat(dto.sessions.first().room.id).isEqualTo(roomId)
        assertThat(dto.sessions.first().room.name).isEqualTo(name)
        assertThat(dto.sessions.first().room.description).isEqualTo(roomDescription)
        assertThat(dto.sessions.first().room.type).isEqualTo(type)
        assertThat(dto.sessions.first().room.capacity).isEqualTo(capacity)
        assertThat(dto.sessions.first().room.accessibility).isEqualTo(accessibility)
    }

    @Test
    fun `should serialize MovieResponseWithSessionsDTO to JSON correctly`() {
        val dto = MovieResponseWithSessionsDTO(
            movie = MovieResponseDTO(
                id = movieId,
                title = title,
                description = movieDescription,
                duration = duration,
                releaseDate = releaseDate,
                isActive = isActive,
                trailerYoutubeId = trailerYoutubeId,
                imageUrl = imageUrl
            ),
            sessions = listOf(
                SessionPartialResponseWithoutMovieDTO(
                    id = sessionId,
                    startTime = sessionStartTime,
                    endTime = sessionEndTime,
                    room = CinemaRoomPartialResponseDTO(
                        id = roomId,
                        name = name,
                        description = roomDescription,
                        type = type,
                        capacity = capacity,
                        accessibility = accessibility
                    )
                )
            )
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, MovieResponseWithSessionsDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, MovieResponseWithSessionsDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to MovieEntity and SessionEntity correctly`() {
        val dto = MovieResponseWithSessionsDTO(
            movie = MovieResponseDTO(
                id = movieId,
                title = title,
                description = movieDescription,
                duration = duration,
                releaseDate = releaseDate,
                isActive = isActive,
                trailerYoutubeId = trailerYoutubeId,
                imageUrl = imageUrl
            ),
            sessions = listOf(
                SessionPartialResponseWithoutMovieDTO(
                    id = sessionId,
                    startTime = sessionStartTime,
                    endTime = sessionEndTime,
                    room = CinemaRoomPartialResponseDTO(
                        id = roomId,
                        name = name,
                        description = roomDescription,
                        type = type,
                        capacity = capacity,
                        accessibility = accessibility
                    )
                )
            )
        )

        val movieWithSessions = dto.toEntity()

        assertThat(movieWithSessions.movie.id).isEqualTo(movieId)
        assertThat(movieWithSessions.movie.title).isEqualTo(title)
        assertThat(movieWithSessions.movie.description).isEqualTo(movieDescription)
        assertThat(movieWithSessions.movie.duration).isEqualTo(duration)
        assertThat(movieWithSessions.movie.releaseDate).isEqualTo(java.time.Instant.parse(releaseDate))
        assertThat(movieWithSessions.movie.isActive).isEqualTo(isActive)
        assertThat(movieWithSessions.movie.trailerYoutubeId).isEqualTo(trailerYoutubeId)
        assertThat(movieWithSessions.movie.imageUrl).isEqualTo(imageUrl)
        assertThat(movieWithSessions.sessions).hasSize(1)
        assertThat(movieWithSessions.sessions.first().id).isEqualTo(sessionId)
        assertThat(movieWithSessions.sessions.first().startTime).isEqualTo(java.time.Instant.parse(sessionStartTime))
        assertThat(movieWithSessions.sessions.first().endTime).isEqualTo(java.time.Instant.parse(sessionEndTime))
        assertThat(movieWithSessions.sessions.first().roomId).isEqualTo(roomId)
    }
}