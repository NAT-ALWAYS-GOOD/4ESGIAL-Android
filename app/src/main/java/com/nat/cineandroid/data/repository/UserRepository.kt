package com.nat.cineandroid.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.nat.cineandroid.data.api.ApiService
import com.nat.cineandroid.data.model.FavoriteTheaterEntity
import com.nat.cineandroid.data.model.ReservationEntity
import com.nat.cineandroid.data.model.UserDAO
import com.nat.cineandroid.data.model.UserEntity
import com.nat.cineandroid.data.model.UserWithReservations
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDAO: UserDAO,
    @ApplicationContext private val context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun getUser(userId: Int): UserWithReservations? {
        return if (isNetworkAvailable()) {
            try {
                val response = apiService.getUser(userId)
                if (response.isSuccessful) {
                    response.body()?.let { userResponse ->
                        val userEntity = UserEntity(
                            id = userResponse.id,
                            username = userResponse.username,
                            password = userResponse.password,
                            isActive = userResponse.isActive,
                            favoriteTheater = userResponse.favoriteTheater?.let {
                                FavoriteTheaterEntity(
                                    id = it.id,
                                    name = it.name,
                                    description = it.description,
                                    streetName = it.streetName,
                                    streetNumber = it.streetNumber,
                                    postalCode = it.postalCode,
                                    city = it.city,
                                    country = it.country,
                                    latitude = it.latitude,
                                    longitude = it.longitude
                                )
                            }
                        )

                        userDAO.insertUser(userEntity)

                        val reservationEntities = userResponse.reservations.map { reservation ->
                            ReservationEntity(
                                reference = reservation.reference,
                                createdAt = reservation.createdAt,
                                qrCode = reservation.qrCode,
                                userId = userResponse.id
                            )
                        }
                        userDAO.insertReservations(reservationEntities)

                        Log.d("UserRepository", "User fetched from network")
                        Log.d("UserRepository", "User: $userEntity")
                        Log.d("UserRepository", "UserDao: ${userDAO.getUser(userId)}")
                        userDAO.getUser(userId)
                    }
                } else {
                    Log.d("UserRepository", "Error fetching user: ${response.code()}, returning cache")
                    userDAO.getUser(userId)
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error fetching user: ${e.message}, returning cache")
                userDAO.getUser(userId)
            }
        } else {
            Log.d("UserRepository", "No network, fetching user from cache")
            userDAO.getUser(userId)
        }
    }
}