package com.nat.cineandroid.core.api

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val jwtTokenProvider: JwtTokenProvider
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.tag(Invocation::class.java)?.method()

        val isAuthenticated = method?.isAnnotationPresent(Authenticated::class.java) == true

        return if (isAuthenticated) {
            val token = jwtTokenProvider.getToken()
            val authenticatedRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}