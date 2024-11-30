package com.nat.cineandroid.data.user.repository

import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.data.user.dao.UserDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDAO: UserDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
)