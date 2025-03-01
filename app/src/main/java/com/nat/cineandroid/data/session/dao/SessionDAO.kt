package com.nat.cineandroid.data.session.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.nat.cineandroid.data.session.entity.ReservationEntity
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.data.session.entity.SessionEntity
import com.nat.cineandroid.data.session.entity.SessionWithSeats
import java.time.Instant

@Dao
interface SessionDAO {
    @Transaction
    @Upsert
    suspend fun upsertSessions(sessions: List<SessionEntity>)

    @Query("SELECT * FROM session")
    suspend fun getSessions(): List<SessionEntity>

    @Query(
        """
    SELECT * FROM session 
    WHERE movieId = :movieId 
      AND roomId IN (SELECT id FROM cinema_room WHERE theaterId = :theaterId)
      AND startTime BETWEEN :today AND :nextWeek
"""
    )
    suspend fun getSessionsWithSeatsFromNowToNextWeek(
        movieId: Int,
        theaterId: Int,
        today: Instant,
        nextWeek: Instant
    ): List<SessionWithSeats>

    @Query("SELECT * FROM reservation WHERE userId = :userId")
    suspend fun getUserReservations(userId: Int): List<ReservationEntity>

    @Upsert
    suspend fun upsertReservation(reservation: ReservationEntity)

    @Upsert
    suspend fun upsertReservations(reservations: List<ReservationEntity>)

    @Query(
        """
        SELECT id FROM session 
        WHERE movieId = :movieId 
          AND roomId IN (SELECT id FROM cinema_room WHERE theaterId = :theaterId)
          AND date(startTime) = :selectedDate
    """
    )
    suspend fun getSessionIds(movieId: Int, theaterId: Int, selectedDate: String): List<Int>

    @Upsert
    suspend fun upsertSeats(seats: List<SeatEntity>)

    @Query("SELECT * FROM session WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Int): SessionEntity
}