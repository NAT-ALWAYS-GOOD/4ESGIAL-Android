package com.nat.cineandroid.data.session

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithSeats(
    @Embedded val session: SessionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val seats: List<SeatEntity>
)
