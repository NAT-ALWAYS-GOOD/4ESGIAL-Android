package com.nat.cineandroid.core.api.nat.dto.movie

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MovieResponseDTOTest {
    private val gson = Gson()

    private val id = 1
    private val title = "Inception"
    private val description = "A mind-bending thriller"
    private val duration = 148
    private val releaseDate = "2025-07-16T00:00:00Z"
    private val isActive = true
    private val trailerYoutubeId = "YoHD9XEInc0"
    private val imageUrl = "https://example.com/inception.jpg"

    private val jsonInput = """
        {
            "id": $id,
            "title": "$title",
            "description": "$description",
            "duration": $duration,
            "releaseDate": "$releaseDate",
            "isActive": $isActive,
            "trailerYoutubeId": "$trailerYoutubeId",
            "imageUrl": "$imageUrl"
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to MovieResponseDTO correctly`() {
        val dto = gson.fromJson(jsonInput, MovieResponseDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.title).isEqualTo(title)
        assertThat(dto.description).isEqualTo(description)
        assertThat(dto.duration).isEqualTo(duration)
        assertThat(dto.releaseDate).isEqualTo(releaseDate)
        assertThat(dto.isActive).isEqualTo(isActive)
        assertThat(dto.trailerYoutubeId).isEqualTo(trailerYoutubeId)
        assertThat(dto.imageUrl).isEqualTo(imageUrl)
    }

    @Test
    fun `should serialize MovieResponseDTO to JSON correctly`() {
        val dto = MovieResponseDTO(
            id = id,
            title = title,
            description = description,
            duration = duration,
            releaseDate = releaseDate,
            isActive = isActive,
            trailerYoutubeId = trailerYoutubeId,
            imageUrl = imageUrl
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, MovieResponseDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, MovieResponseDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }

    @Test
    fun `should map to MovieEntity correctly`() {
        val dto = MovieResponseDTO(
            id = id,
            title = title,
            description = description,
            duration = duration,
            releaseDate = releaseDate,
            isActive = isActive,
            trailerYoutubeId = trailerYoutubeId,
            imageUrl = imageUrl
        )

        val entity = dto.toMovieEntity()

        assertThat(entity.id).isEqualTo(id)
        assertThat(entity.title).isEqualTo(title)
        assertThat(entity.description).isEqualTo(description)
        assertThat(entity.duration).isEqualTo(duration)
        assertThat(entity.releaseDate.toString()).isEqualTo(releaseDate)
        assertThat(entity.isActive).isEqualTo(isActive)
        assertThat(entity.trailerYoutubeId).isEqualTo(trailerYoutubeId)
        assertThat(entity.imageUrl).isEqualTo(imageUrl)
    }
}