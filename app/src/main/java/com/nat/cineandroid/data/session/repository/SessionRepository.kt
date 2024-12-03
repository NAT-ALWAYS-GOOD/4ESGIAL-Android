package com.nat.cineandroid.data.session.repository

import com.nat.cineandroid.core.api.HttpClient
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.data.session.dao.SessionDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDAO: SessionDAO,
    private val apiService: NATCinemasAPI,
    private val httpClient: HttpClient
)