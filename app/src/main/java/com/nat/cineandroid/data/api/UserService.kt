package com.nat.cineandroid.data.api

import com.nat.cineandroid.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): Response<UserResponse>
}