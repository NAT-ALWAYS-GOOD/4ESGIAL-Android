package com.nat.cineandroid.core.api.nat.dto.session

import com.google.gson.Gson
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomPartialResponseDTO
import com.nat.cineandroid.core.api.nat.dto.cinemaRoom.CinemaRoomResponseDTO
import com.nat.cineandroid.core.api.nat.dto.movie.MovieResponseDTO
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant

class ReservationResponseDTOTest {
    private val gson = Gson()

    private val reference = "AZERTY12345"
    private val createdAt = "2025-06-01T12:00:00Z"
    private val qrCode = "QRCODE12345"
    private val seats = listOf(1, 2, 3)

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

    private val userId = 1

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

    private val jsonInput = """
        {
            "reference": "$reference",
            "createdAt": "$createdAt",
            "qrCode": "$qrCode",
            "seats": [${seats.joinToString()}],
            "userId": $userId,
            "session": {
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
                    "accessibility": $accessibility
                }
            },
            "theaterEntity": {
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
    fun `should deserialize JSON to ReservationResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, ReservationResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.reference).isEqualTo(reference)
        assertThat(dto.createdAt).isEqualTo(createdAt)
        assertThat(dto.qrCode).isEqualTo(qrCode)
        assertThat(dto.seats).isEqualTo(seats)
        assertThat(dto.session.id).isEqualTo(sessionId)
        assertThat(dto.session.startTime).isEqualTo(startTime)
        assertThat(dto.session.endTime).isEqualTo(endTime)
        assertThat(dto.session.movie.id).isEqualTo(movieId)
        assertThat(dto.session.movie.title).isEqualTo(title)
        assertThat(dto.session.movie.description).isEqualTo(movieDescription)
        assertThat(dto.session.movie.duration).isEqualTo(duration)
        assertThat(dto.session.movie.releaseDate).isEqualTo(releaseDate)
        assertThat(dto.session.movie.isActive).isEqualTo(isActive)
        assertThat(dto.session.movie.trailerYoutubeId).isEqualTo(trailerYoutubeId)
        assertThat(dto.session.movie.imageUrl).isEqualTo(imageUrl)
        assertThat(dto.session.room.id).isEqualTo(roomId)
        assertThat(dto.session.room.name).isEqualTo(roomName)
        assertThat(dto.session.room.description).isEqualTo(roomDescription)
        assertThat(dto.session.room.type).isEqualTo(type)
        assertThat(dto.session.room.capacity).isEqualTo(capacity)
        assertThat(dto.session.room.accessibility).isEqualTo(accessibility)
        assertThat(dto.theaterEntity.id).isEqualTo(theaterId)
        assertThat(dto.theaterEntity.name).isEqualTo(theaterName)
        assertThat(dto.theaterEntity.description).isEqualTo(theaterDescription)
        assertThat(dto.theaterEntity.streetName).isEqualTo(streetName)
        assertThat(dto.theaterEntity.streetNumber).isEqualTo(streetNumber)
        assertThat(dto.theaterEntity.postalCode).isEqualTo(postalCode)
        assertThat(dto.theaterEntity.city).isEqualTo(city)
        assertThat(dto.theaterEntity.country).isEqualTo(country)
        assertThat(dto.theaterEntity.latitude).isEqualTo(latitude)
        assertThat(dto.theaterEntity.longitude).isEqualTo(longitude)
    }

    @Test
    fun `should serialize ReservationResponseDTO to JSON correctly`() {
        val dto = ReservationResponseDTO(
            reference = reference,
            createdAt = createdAt,
            qrCode = qrCode,
            seats = seats,
            userId = userId,
            session = SessionPartialResponseDTO(
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
                room = CinemaRoomPartialResponseDTO(
                    id = roomId,
                    name = roomName,
                    description = roomDescription,
                    type = type,
                    capacity = capacity,
                    accessibility = accessibility
                )
            ),
            theaterEntity = TheaterEntity(
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

        val dtoFromOutput = gson.fromJson(jsonOutput, ReservationResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, ReservationResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to ReservationEntity correctly`() {
        val dto = ReservationResponseDTO(
            reference = reference,
            createdAt = createdAt,
            qrCode = qrCode,
            seats = seats,
            userId = userId,
            session = SessionPartialResponseDTO(
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
                room = CinemaRoomPartialResponseDTO(
                    id = roomId,
                    name = roomName,
                    description = roomDescription,
                    type = type,
                    capacity = capacity,
                    accessibility = accessibility
                )
            ),
            theaterEntity = TheaterEntity(
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

        val entity = dto.toReservationEntity()

        assertThat(entity.reference).isEqualTo(reference)
        assertThat(entity.createdAt).isEqualTo(Instant.parse(createdAt))
        assertThat(entity.qrCode).isEqualTo(qrCode)
        assertThat(entity.seats).isEqualTo(seats)
        assertThat(entity.userId).isEqualTo(userId)
        assertThat(entity.sessionId).isEqualTo(sessionId)
    }

}