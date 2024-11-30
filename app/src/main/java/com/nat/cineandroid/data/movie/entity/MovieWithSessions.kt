package com.nat.cineandroid.data.movie.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.nat.cineandroid.data.session.entity.SessionEntity

data class MovieWithSessions(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val sessions: List<SessionEntity>
)
