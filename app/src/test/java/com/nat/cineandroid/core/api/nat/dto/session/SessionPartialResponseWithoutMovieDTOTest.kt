package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant

class SessionPartialResponseWithoutMovieDTOTest {
    private val gson = Gson()

    private val sessionId = 1
    private val startTime = "2025-06-01T12:00:00Z"
    private val endTime = "2025-06-01T14:00:00Z"

    private val movieId = 1

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
    fun `should deserialize JSON to SessionPartialResponseWithoutMovieDTO correctly`() {
        val dto = gson.fromJson(jsonInput, SessionPartialResponseWithoutMovieDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(sessionId)
        assertThat(dto.startTime).isEqualTo(startTime)
        assertThat(dto.endTime).isEqualTo(endTime)
        assertThat(dto.room.id).isEqualTo(roomId)
        assertThat(dto.room.name).isEqualTo(roomName)
        assertThat(dto.room.description).isEqualTo(roomDescription)
        assertThat(dto.room.type).isEqualTo(type)
        assertThat(dto.room.capacity).isEqualTo(capacity)
        assertThat(dto.room.accessibility).isEqualTo(accessibility)
    }

    @Test
    fun `should serialize SessionPartialResponseWithoutMovieDTO to JSON correctly`() {
        val dto = SessionPartialResponseWithoutMovieDTO(
            id = sessionId,
            startTime = startTime,
            endTime = endTime,
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

        val dtoFromOutput = gson.fromJson(jsonOutput, SessionPartialResponseWithoutMovieDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, SessionPartialResponseWithoutMovieDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to SessionEntity correctly`() {
        val dto = SessionPartialResponseWithoutMovieDTO(
            id = sessionId,
            startTime = startTime,
            endTime = endTime,
            room = CinemaRoomPartialResponseDTO(
                id = roomId,
                name = roomName,
                description = roomDescription,
                type = type,
                capacity = capacity,
                accessibility = accessibility
            )
        )

        val entity = dto.toSessionEntity(movieId)

        assertThat(entity.id).isEqualTo(sessionId)
        assertThat(entity.startTime).isEqualTo(Instant.parse(startTime))
        assertThat(entity.endTime).isEqualTo(Instant.parse(endTime))
        assertThat(entity.movieId).isEqualTo(movieId)
        assertThat(entity.roomId).isEqualTo(roomId)
    }
}