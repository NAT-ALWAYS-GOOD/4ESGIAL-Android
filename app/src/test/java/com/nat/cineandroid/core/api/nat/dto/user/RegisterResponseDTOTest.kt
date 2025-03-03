package com.nat.cineandroid.core.api.nat.dto.user

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.session.ReservationPartialResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RegisterResponseDTOTest {
    private val gson = Gson()

    private val userId = 1
    private val username = "username"
    private val password = "password"
    private val isActive = true
    private val favoriteTheater = null

    private val reference = "AZERTY12345"
    private val createdAt = "2025-06-01T12:00:00Z"
    private val qrCode = "QRCODE12345"

    private val accessToken = "accessToken"

    private val jsonInput = """
        {
            "user": {
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
            },
            "access_token": "$accessToken"
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to RegisterResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, RegisterResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.user.id).isEqualTo(userId)
        assertThat(dto.user.username).isEqualTo(username)
        assertThat(dto.user.password).isEqualTo(password)
        assertThat(dto.user.isActive).isEqualTo(isActive)
        assertThat(dto.user.favoriteTheater).isEqualTo(favoriteTheater)
        assertThat(dto.user.reservations).hasSize(1)
        assertThat(dto.user.reservations[0].reference).isEqualTo(reference)
        assertThat(dto.user.reservations[0].createdAt).isEqualTo(createdAt)
        assertThat(dto.user.reservations[0].qrCode).isEqualTo(qrCode)
        assertThat(dto.accessToken).isEqualTo(accessToken)
    }

    @Test
    fun `should serialize RegisterResponseDTO to JSON correctly`() {
        val dto = RegisterResponseDTO(
            user = UserResponseDTO(
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
            ),
            accessToken = accessToken
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, RegisterResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, RegisterResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }
}