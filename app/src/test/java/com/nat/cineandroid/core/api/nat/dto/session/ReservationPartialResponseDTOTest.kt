package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ReservationPartialResponseDTOTest {
    private val gson = Gson()

    private val reference = "AZERTY12345"
    private val createdAt = "2025-06-01T12:00:00Z"
    private val qrCode = "QRCODE12345"

    private val jsonInput = """
        {
            "reference": "$reference",
            "createdAt": "$createdAt",
            "qrCode": "$qrCode"
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to ReservationPartialResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, ReservationPartialResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.reference).isEqualTo(reference)
        assertThat(dto.createdAt).isEqualTo(createdAt)
        assertThat(dto.qrCode).isEqualTo(qrCode)
    }

    @Test
    fun `should serialize ReservationPartialResponseDTO to JSON correctly`() {
        val dto = ReservationPartialResponseDTO(
            reference = reference,
            createdAt = createdAt,
            qrCode = qrCode
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, ReservationPartialResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, ReservationPartialResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }
}