package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.Authenticated
import com.nat.cineandroid.core.api.nat.dto.session.ReservationResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SessionService {
    @GET("sessions/reservation/user/{userId}")
    @Authenticated
    suspend fun getUserReservations(@Path("userId") userId: Int): Response<List<ReservationResponseDTO>>
}