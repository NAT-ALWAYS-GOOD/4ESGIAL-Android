package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.user.LoginRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
/*    @GET("users/{userId}")
    @Authenticated
    suspend fun getUser(@Path("userId") userId: Int): Response<UserResponse>
*/
    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>
}