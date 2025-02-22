package com.nat.cineandroid.data.movie.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.movie.entity.MovieWithSessions

@Dao
interface MovieDAO {
    @Transaction
    @Query(
        """
    SELECT *
    FROM movie
    WHERE id IN (
        SELECT DISTINCT movieId
        FROM session
        WHERE roomId IN (
            SELECT id
            FROM cinema_room
            WHERE theaterId = :theaterId
        )
    )
"""
    )
    suspend fun getMoviesByTheaterId(theaterId: Int): List<MovieWithSessions>

    @Transaction
    @Upsert
    suspend fun upsertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movie")
    suspend fun getMovies(): List<MovieEntity>

    // get movies not released yet
    @Query("SELECT * FROM movie WHERE releaseDate > CURRENT_DATE")
    suspend fun getNotReleasedMovies(): List<MovieEntity>
}