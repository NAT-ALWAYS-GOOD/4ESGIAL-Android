package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SeatResponseDTOTest {
    private val gson = Gson()

    private val seatNumber = 1
    private val isReserved = true

    private val jsonInput = """
        {
            "seatNumber": $seatNumber,
            "isReserved": $isReserved
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to SeatResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, SeatResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.seatNumber).isEqualTo(seatNumber)
        assertThat(dto.isReserved).isEqualTo(isReserved)
    }

    @Test
    fun `should serialize SeatResponseDTO to JSON correctly`() {
        val dto = SeatResponseDTO(
            seatNumber = seatNumber,
            isReserved = isReserved
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, SeatResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, SeatResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }
}