package com.nat.cineandroid.data.user.repository

import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.core.api.JwtTokenProvider
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.api.nat.dto.user.LoginRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.LoginResponseDTO
import com.nat.cineandroid.core.api.nat.dto.user.UserResponseDTO
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
    suspend fun login(email: String, password: String): HttpResult<UserEntity> {
        val loginRequest = LoginRequestDTO(email, password)
        return httpClient.authenticate(
            networkCall = { apiService.login(loginRequest) },
            saveToken = { jwtTokenProvider.saveToken(it.accessToken) },
            saveToCache = { userEntity -> userDAO.insert(userEntity) },
            transformResponse = { it.user.toUserEntity() }
        )
    }
}