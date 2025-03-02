package com.nat.cineandroid.core.api.nat.dto.theater

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheaterResponseDTOTest {
    private val gson = Gson()

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
    """.trimIndent()

    @Test
    fun `should deserialize JSON to TheaterResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, TheaterResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(theaterId)
        assertThat(dto.name).isEqualTo(theaterName)
        assertThat(dto.description).isEqualTo(theaterDescription)
        assertThat(dto.streetName).isEqualTo(streetName)
        assertThat(dto.streetNumber).isEqualTo(streetNumber)
        assertThat(dto.postalCode).isEqualTo(postalCode)
        assertThat(dto.city).isEqualTo(city)
        assertThat(dto.country).isEqualTo(country)
        assertThat(dto.latitude).isEqualTo(latitude)
        assertThat(dto.longitude).isEqualTo(longitude)
    }

    @Test
    fun `should serialize TheaterResponseDTO to JSON correctly`() {
        val dto = TheaterResponseDTO(
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

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, TheaterResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, TheaterResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to TheaterEntity correctly`() {
        val dto = TheaterResponseDTO(
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

        val entity = dto.toTheaterEntity()

        assertThat(entity.id).isEqualTo(theaterId)
        assertThat(entity.name).isEqualTo(theaterName)
        assertThat(entity.description).isEqualTo(theaterDescription)
        assertThat(entity.streetName).isEqualTo(streetName)
        assertThat(entity.streetNumber).isEqualTo(streetNumber)
        assertThat(entity.postalCode).isEqualTo(postalCode)
        assertThat(entity.city).isEqualTo(city)
        assertThat(entity.country).isEqualTo(country)
        assertThat(entity.latitude).isEqualTo(latitude)
        assertThat(entity.longitude).isEqualTo(longitude)
    }
}