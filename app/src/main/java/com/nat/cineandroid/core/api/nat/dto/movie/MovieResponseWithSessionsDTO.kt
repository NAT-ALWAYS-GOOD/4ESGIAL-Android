package com.nat.cineandroid.core.api.nat.dto.movie

import com.google.gson.annotations.SerializedName
import com.nat.cineandroid.core.api.nat.dto.session.SessionPartialResponseWithoutMovieDTO
import com.nat.cineandroid.data.movie.entity.MovieWithSessions

data class MovieResponseWithSessionsDTO(
    @SerializedName("movie")
    val movie: MovieResponseDTO,
    @SerializedName("sessions")
    val sessions: List<SessionPartialResponseWithoutMovieDTO>
) {
    fun toEntity(): MovieWithSessions {
        return MovieWithSessions(
            movie = movie.toMovieEntity(),
            sessions = sessions.map { it.toSessionEntity(movie.id) }
        )
    }
}