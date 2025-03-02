package com.nat.cineandroid.core.api.nat.dto.cinemaRoom

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CinemaRoomPartialResponseDTOTest {
    private val gson = Gson()

    private val id = 1
    private val name = "Salle 1"
    private val description = "Description Salle 1"
    private val type = "Standard"
    private val capacity = 100
    private val accessibility = true

    private val jsonInput = """
        {
            "id": $id,
            "name": "$name",
            "description": "$description",
            "type": "$type",
            "capacity": $capacity,
            "accessibility": $accessibility
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to CinemaRoomPartialResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, CinemaRoomPartialResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.name).isEqualTo(name)
        assertThat(dto.description).isEqualTo(description)
        assertThat(dto.type).isEqualTo(type)
        assertThat(dto.capacity).isEqualTo(capacity)
        assertThat(dto.accessibility).isEqualTo(accessibility)
    }

    @Test
    fun `should serialize CinemaRoomPartialResponseDTO to JSON correctly`() {
        val dto = CinemaRoomPartialResponseDTO(
            id = id,
            name = name,
            description = description,
            type = type,
            capacity = capacity,
            accessibility = accessibility
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, CinemaRoomPartialResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, CinemaRoomPartialResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to CinemaRoomEntity correctly`() {
        val dto = CinemaRoomPartialResponseDTO(
            id = id,
            name = name,
            description = description,
            type = type,
            capacity = capacity,
            accessibility = accessibility
        )

        val theaterId = 2
        val entity = dto.toCinemaRoomEntity(theaterId)

        assertThat(entity.id).isEqualTo(id)
        assertThat(entity.name).isEqualTo(name)
        assertThat(entity.description).isEqualTo(description)
        assertThat(entity.type).isEqualTo(type)
        assertThat(entity.capacity).isEqualTo(capacity)
        assertThat(entity.isAccessible).isEqualTo(accessibility)
        assertThat(entity.theaterId).isEqualTo(theaterId)
    }
}