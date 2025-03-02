package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant

class SessionPartialResponseDTOTest {
    private val gson = Gson()

    private val sessionId = 1
    private val startTime = "2025-06-01T12:00:00Z"
    private val endTime = "2025-06-01T14:00:00Z"

    private val movieId = 1
    private val title = "Inception"
    private val movieDescription = "A mind-bending thriller"
    private val duration = 148
    private val releaseDate = "2025-07-16T00:00:00Z"
    private val isActive = true
    private val trailerYoutubeId = "YoHD9XEInc0"
    private val imageUrl = "https://example.com/inception.jpg"

    private val roomId = 1
    private val roomName = "Salle 1"
    private val roomDescription = "Description Salle 1"
    private val type = "Standard"
    private val capacity = 100
    private val accessibility = true

    private val jsonInput = """
        {
            "id": $sessionId,
            "startTime": "$startTime",
            "endTime": "$endTime",
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
            "room": {
                "id": $roomId,
                "name": "$roomName",
                "description": "$roomDescription",
                "type": "$type",
                "capacity": $capacity,
                "accessibility": $accessibility
            }
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to SessionPartialResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, SessionPartialResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(sessionId)
        assertThat(dto.startTime).isEqualTo(startTime)
        assertThat(dto.endTime).isEqualTo(endTime)
        assertThat(dto.movie.id).isEqualTo(movieId)
        assertThat(dto.movie.title).isEqualTo(title)
        assertThat(dto.movie.description).isEqualTo(movieDescription)
        assertThat(dto.movie.duration).isEqualTo(duration)
        assertThat(dto.movie.releaseDate).isEqualTo(releaseDate)
        assertThat(dto.movie.isActive).isEqualTo(isActive)
        assertThat(dto.movie.trailerYoutubeId).isEqualTo(trailerYoutubeId)
        assertThat(dto.movie.imageUrl).isEqualTo(imageUrl)
        assertThat(dto.room.id).isEqualTo(roomId)
        assertThat(dto.room.name).isEqualTo(roomName)
        assertThat(dto.room.description).isEqualTo(roomDescription)
        assertThat(dto.room.type).isEqualTo(type)
        assertThat(dto.room.capacity).isEqualTo(capacity)
        assertThat(dto.room.accessibility).isEqualTo(accessibility)
    }

    @Test
    fun `should serialize SessionPartialResponseDTO to JSON correctly`() {
        val dto = SessionPartialResponseDTO(
            id = sessionId,
            startTime = startTime,
            endTime = endTime,
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
            room = CinemaRoomPartialResponseDTO(
                id = roomId,
                name = roomName,
                description = roomDescription,
                type = type,
                capacity = capacity,
                accessibility = accessibility
            )
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, SessionPartialResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, SessionPartialResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to SessionEntity correctly`() {
        val dto = SessionPartialResponseDTO(
            id = sessionId,
            startTime = startTime,
            endTime = endTime,
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
            room = CinemaRoomPartialResponseDTO(
                id = roomId,
                name = roomName,
                description = roomDescription,
                type = type,
                capacity = capacity,
                accessibility = accessibility
            )
        )

        val entity = dto.toSessionEntity()

        assertThat(entity.id).isEqualTo(sessionId)
        assertThat(entity.startTime).isEqualTo(Instant.parse(startTime))
        assertThat(entity.endTime).isEqualTo(Instant.parse(endTime))
        assertThat(entity.movieId).isEqualTo(movieId)
        assertThat(entity.roomId).isEqualTo(roomId)
    }
}