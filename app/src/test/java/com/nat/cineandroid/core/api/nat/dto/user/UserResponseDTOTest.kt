package com.nat.cineandroid.core.api.nat.dto.user

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.session.ReservationPartialResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserResponseDTOTest {
    private val gson = Gson()

    private val userId = 1
    private val username = "username"
    private val password = "password"
    private val isActive = true
    private val favoriteTheater = null

    private val reference = "AZERTY12345"
    private val createdAt = "2025-06-01T12:00:00Z"
    private val qrCode = "QRCODE12345"

    private val jsonInput = """
        {
            "id": $userId,
            "username": "$username",
            "password": "$password",
            "isActive": $isActive,
            "favoriteTheater": $favoriteTheater,
            "reservations": [
                {
                    "reference": "$reference",
                    "createdAt": "$createdAt",
                    "qrCode": "$qrCode"
                }
            ]
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to UserResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, UserResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(userId)
        assertThat(dto.username).isEqualTo(username)
        assertThat(dto.password).isEqualTo(password)
        assertThat(dto.isActive).isEqualTo(isActive)
        assertThat(dto.favoriteTheater).isEqualTo(favoriteTheater)
        assertThat(dto.reservations).hasSize(1)
        assertThat(dto.reservations[0].reference).isEqualTo(reference)
        assertThat(dto.reservations[0].createdAt).isEqualTo(createdAt)
        assertThat(dto.reservations[0].qrCode).isEqualTo(qrCode)
    }

    @Test
    fun `should serialize UserResponseDTO to JSON correctly`() {
        val dto = UserResponseDTO(
            id = userId,
            username = username,
            password = password,
            isActive = isActive,
            favoriteTheater = favoriteTheater,
            reservations = listOf(
                ReservationPartialResponseDTO(
                    reference = reference,
                    createdAt = createdAt,
                    qrCode = qrCode
                )
            )
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, UserResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, UserResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to UserEntity correctly`() {
        val dto = UserResponseDTO(
            id = userId,
            username = username,
            password = password,
            isActive = isActive,
            favoriteTheater = favoriteTheater,
            reservations = listOf(
                ReservationPartialResponseDTO(
                    reference = reference,
                    createdAt = createdAt,
                    qrCode = qrCode
                )
            )
        )

        val entity = dto.toUserEntity()

        assertThat(entity.id).isEqualTo(userId)
        assertThat(entity.username).isEqualTo(username)
        assertThat(entity.isActive).isEqualTo(isActive)
        assertThat(entity.favoriteTheaterId).isEqualTo(favoriteTheater)
    }
}