package com.nat.cineandroid.core.api.nat.dto.cinemaRoom

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CinemaRoomResponseDTOTest {
    private val gson = Gson()

    private val id = 1
    private val name = "Salle 1"
    private val description = "Description Salle 1"
    private val type = "Standard"
    private val capacity = 100
    private val accessibility = true

    private val theaterId = 10
    private val theaterName = "Theater 1"
    private val theaterDescription = "Description Theater 1"
    private val streetName = "Street"
    private val streetNumber = "123"
    private val postalCode = "75001"
    private val city = "Paris"
    private val country = "France"
    private val latitude = 48.8566
    private val longitude = 2.3522

    private val jsonInput = """
        {
            "id": $id,
            "name": "$name",
            "description": "$description",
            "type": "$type",
            "capacity": $capacity,
            "accessibility": $accessibility,
            "theater": {
                "id": $theaterId,
                "name": "$theaterName",
                "description": "$theaterDescription",
                "streetName": "$streetName",
                "streetNumber": "$streetNumber",
                "postalCode": "$postalCode",
                "city": "$city",
                "country": "$country",
                "latitude": $latitude,
                "longitude": $longitude
            }
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to CinemaRoomResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, CinemaRoomResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.name).isEqualTo(name)
        assertThat(dto.description).isEqualTo(description)
        assertThat(dto.type).isEqualTo(type)
        assertThat(dto.capacity).isEqualTo(capacity)
        assertThat(dto.accessibility).isEqualTo(accessibility)
        assertThat(dto.theater.id).isEqualTo(theaterId)
        assertThat(dto.theater.name).isEqualTo(theaterName)
        assertThat(dto.theater.description).isEqualTo(theaterDescription)
        assertThat(dto.theater.streetName).isEqualTo(streetName)
        assertThat(dto.theater.streetNumber).isEqualTo(streetNumber)
        assertThat(dto.theater.postalCode).isEqualTo(postalCode)
        assertThat(dto.theater.city).isEqualTo(city)
        assertThat(dto.theater.country).isEqualTo(country)
        assertThat(dto.theater.latitude).isEqualTo(latitude)
        assertThat(dto.theater.longitude).isEqualTo(longitude)
    }

    @Test
    fun `should serialize CinemaRoomResponseDTO to JSON correctly`() {
        val dto = CinemaRoomResponseDTO(
            id = id,
            name = name,
            description = description,
            type = type,
            capacity = capacity,
            accessibility = accessibility,
            theater = TheaterResponseDTO(
                id = theaterId,
                name = theaterName,
                description = theaterDescription,
                streetName = streetName,
                streetNumber = streetNumber,
                postalCode = postalCode,
                city = city,
                country = country,
                latitude = latitude,
                longitude = longitude
            )
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, CinemaRoomResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, CinemaRoomResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }
}