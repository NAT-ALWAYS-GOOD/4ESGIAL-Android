package com.nat.cineandroid.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithReservations(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val reservations: List<ReservationEntity>
)

