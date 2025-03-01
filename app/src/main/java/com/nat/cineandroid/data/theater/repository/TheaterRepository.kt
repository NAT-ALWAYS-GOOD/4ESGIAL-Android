package com.nat.cineandroid.data.theater.repository

import android.util.Log
import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheaterRepository @Inject constructor(
    private val theaterDAO: TheaterDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
) {
    suspend fun getTheaters(): HttpResult<List<TheaterEntity>> {
        return httpClient.fetchData(
            networkCall = {
                apiService.getTheaters()
            },
            cacheCall = {
                Log.d("TheaterRepository", "Cache response: ${theaterDAO.getTheaters()}")
                theaterDAO.getTheaters()
            },
            saveToCache = { theaters: List<TheaterEntity> ->
                theaterDAO.upsertTheaters(theaters)
                Log.d("TheaterRepository", "Saved data: ${theaterDAO.getTheaters()}")
            },
            transformResponse = { dtoList ->
                dtoList.map { it.toTheaterEntity() }
            }
        )
    }

    suspend fun getTheaterFromCache(theaterId: Int): TheaterEntity {
        return theaterDAO.getTheaterById(theaterId)
    }
}