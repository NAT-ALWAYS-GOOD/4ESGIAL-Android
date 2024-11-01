package com.nat.cineandroid.data.repository

import android.content.Context
import com.nat.cineandroid.data.api.ApiService
import com.nat.cineandroid.data.model.UserDAO
import com.nat.cineandroid.data.model.UserWithReservations
import com.nat.cineandroid.data.utils.ApiResult
import com.nat.cineandroid.data.utils.BaseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDAO: UserDAO,
    @ApplicationContext private val context: Context
) : BaseRepository(context) {
    suspend fun getUser(userId: Int): ApiResult<UserWithReservations> {
        return fetchData(
            networkCall = { apiService.getUser(userId) },
            cacheCall = { userDAO.getUser(userId) },
            saveToCache = { userWithReservations ->
                userDAO.insertUser(userWithReservations.user)
                userDAO.insertReservations(userWithReservations.reservations)
            },
            transformResponse = { userWithReservations ->
                val userEntity = userWithReservations.toUserEntity()
                val userReservations = userWithReservations.reservations.map { reservation ->
                    reservation.toReservationEntity(userId)
                }
                UserWithReservations(userEntity, userReservations)
            }
        )
    }
}