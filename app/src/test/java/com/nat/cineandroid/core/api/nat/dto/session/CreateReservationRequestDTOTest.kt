package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CreateReservationRequestDTOTest {
    val gson = Gson()

    private val sessionId = 1
    private val userId = 1
    private val seats = listOf(1, 2, 3)

    private val jsonInput = """
        {
            "sessionId": $sessionId,
            "userId": $userId,
            "seats": [1, 2, 3]
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to CreateReservationRequestDTO correctly`() {
        val dto = gson.fromJson(jsonInput, CreateResesrvationRequestDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.sessionId).isEqualTo(sessionId)
        assertThat(dto.userId).isEqualTo(userId)
        assertThat(dto.seats).isEqualTo(seats)
    }

    @Test
    fun `should serialize CreateReservationRequestDTO to JSON correctly`() {
        val dto = CreateResesrvationRequestDTO(
            sessionId = sessionId,
            userId = userId,
            seats = seats
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, CreateResesrvationRequestDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, CreateResesrvationRequestDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }
}