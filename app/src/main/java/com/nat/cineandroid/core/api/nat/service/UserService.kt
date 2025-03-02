package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.user.LoginRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.LoginResponseDTO
import com.nat.cineandroid.core.api.nat.dto.user.RegisterRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.RegisterResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("users/signup")
    suspend fun register(@Body registerRequest: RegisterRequestDTO): Response<RegisterResponseDTO>
}