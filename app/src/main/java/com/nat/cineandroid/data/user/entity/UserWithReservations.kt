package com.nat.cineandroid.data.user.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.nat.cineandroid.data.session.entity.ReservationEntity

data class UserWithReservations(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val reservations: List<ReservationEntity>
)