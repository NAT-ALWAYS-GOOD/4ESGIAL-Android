package com.nat.cineandroid.data.movie

import androidx.room.Embedded
import androidx.room.Relation
import com.nat.cineandroid.data.session.SessionEntity

data class MovieWithSessions(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val sessions: List<SessionEntity>
)
