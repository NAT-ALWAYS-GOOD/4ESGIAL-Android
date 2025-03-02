package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import com.nat.cineandroid.core.api.nat.dto.theater.TheaterResponseDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant

class SessionResponseDTOTest {
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

    private val seatNumber1 = 1
    private val isReserved1 = true
    private val seatNumber2 = 2
    private val isReserved2 = false

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
            },
            "seats": [
                {
                    "seatNumber": $seatNumber1,
                    "isReserved": $isReserved1
                },
                {
                    "seatNumber": $seatNumber2,
                    "isReserved": $isReserved2
                }
            ]
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to SessionResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, SessionResponseDTO::class.java)

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
        assertThat(dto.room.theater.id).isEqualTo(theaterId)
        assertThat(dto.room.theater.name).isEqualTo(theaterName)
        assertThat(dto.room.theater.description).isEqualTo(theaterDescription)
        assertThat(dto.room.theater.streetName).isEqualTo(streetName)
        assertThat(dto.room.theater.streetNumber).isEqualTo(streetNumber)
        assertThat(dto.room.theater.postalCode).isEqualTo(postalCode)
        assertThat(dto.room.theater.city).isEqualTo(city)
        assertThat(dto.room.theater.country).isEqualTo(country)
        assertThat(dto.room.theater.latitude).isEqualTo(latitude)
        assertThat(dto.room.theater.longitude).isEqualTo(longitude)
        assertThat(dto.seats).hasSize(2)
        assertThat(dto.seats[0].seatNumber).isEqualTo(seatNumber1)
        assertThat(dto.seats[0].isReserved).isEqualTo(isReserved1)
        assertThat(dto.seats[1].seatNumber).isEqualTo(seatNumber2)
        assertThat(dto.seats[1].isReserved).isEqualTo(isReserved2)
    }

    @Test
    fun `should serialize SessionResponseDTO to JSON correctly`() {
        val dto = SessionResponseDTO(
            id = sessionId,
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
            room = CinemaRoomResponseDTO(
                id = roomId,
                name = roomName,
                description = roomDescription,
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
            ),
            startTime = startTime,
            endTime = endTime,
            seats = listOf(
                SeatResponseDTO(
                    seatNumber = seatNumber1,
                    isReserved = isReserved1
                ),
                SeatResponseDTO(
                    seatNumber = seatNumber2,
                    isReserved = isReserved2
                )
            )
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, SessionResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, SessionResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to SessionEntity correctly`() {
        val dto = SessionResponseDTO(
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
            room = CinemaRoomResponseDTO(
                id = roomId,
                name = roomName,
                description = roomDescription,
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
            ),
            seats = listOf(
                SeatResponseDTO(
                    seatNumber = seatNumber1,
                    isReserved = isReserved1
                ),
                SeatResponseDTO(
                    seatNumber = seatNumber2,
                    isReserved = isReserved2
                )
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