package com.nat.cineandroid.data.user.repository

import android.util.Log
import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.core.api.JwtTokenProvider
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.api.nat.dto.user.LoginRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.RegisterRequestDTO
import com.nat.cineandroid.data.user.dao.UserDAO
import com.nat.cineandroid.data.user.entity.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDAO: UserDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient,
    private val jwtTokenProvider: JwtTokenProvider
) {
    suspend fun login(username: String, password: String): HttpResult<UserEntity> {
        val loginRequest = LoginRequestDTO(username, password)
        return httpClient.authenticate(
            networkCall = { apiService.login(loginRequest) },
            saveToken = { jwtTokenProvider.saveToken(it.accessToken) },
            saveToCache = { userEntity -> userDAO.insert(userEntity) },
            transformResponse = { it.user.toUserEntity() }
        )
    }

    suspend fun register(username: String, password: String): HttpResult<UserEntity> {
        val registerRequest = RegisterRequestDTO(username, password)
        return httpClient.authenticate(
            networkCall = { apiService.register(registerRequest) },
            saveToken = { jwtTokenProvider.saveToken(it.accessToken) },
            saveToCache = { userEntity -> userDAO.insert(userEntity) },
            transformResponse = { it.user.toUserEntity() }
        )
    }

    suspend fun getUser(userId: Int): UserEntity {
        return userDAO.getUser(userId)
    }

    suspend fun updateFavoriteTheater(userId: Int, theaterId: Int): HttpResult<Unit> {
        return httpClient.updateData(
            networkCall = { apiService.updateFavoriteTheater(theaterId, userId) },
            updateCache = {
                val user = userDAO.getUser(userId)
                val updateUser = user.copy(favoriteTheaterId = theaterId)
                userDAO.updateUser(updateUser)
            },
        )
    }
}