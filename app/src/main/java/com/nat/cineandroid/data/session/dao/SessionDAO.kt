package com.nat.cineandroid.data.session.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.nat.cineandroid.data.session.entity.SessionEntity

@Dao
interface SessionDAO {
    @Transaction
    @Upsert
    suspend fun upsertSessions(sessions: List<SessionEntity>)

    @Query("SELECT * FROM session")
    suspend fun getSessions(): List<SessionEntity>
}