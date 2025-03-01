package com.nat.cineandroid.core.api

import android.util.Log
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpClient @Inject constructor(
    private val networkHelper: NetworkHelper
) {
    suspend fun <T, R> fetchData(
        networkCall: suspend () -> Response<T>,
        cacheCall: suspend () -> R? = { null },
        saveToCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (networkHelper.isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) HttpResult.HttpError(
                    response.code(),
                    response.message()
                )
                response.body()?.let { data ->
                    val transformedData = transformResponse(data)
                    saveToCache(transformedData)
                    HttpResult.Success(transformedData)
                } ?: HttpResult.NoData("No data")
            } catch (e: Exception) {
                Log.d("HttpClient", "Error fetching data: ${e.message}")
                val dataFromCache = cacheCall()
                dataFromCache?.let { HttpResult.Success(it) }
                    ?: HttpResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            val dataFromCache = cacheCall()
            dataFromCache?.let { HttpResult.Success(it) }
                ?: HttpResult.NetworkError("No network connection and no data in cache")
        }
    }

    suspend fun <T, R> createData(
        networkCall: suspend () -> Response<T>,
        saveToCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (networkHelper.isNetworkAvailable()) {
            try {
                val response = networkCall()
                Log.d("Network", "Response code: ${response.code()} - Body: ${response.errorBody()?.string() ?: response.body()?.toString()}")
                if (!response.isSuccessful) HttpResult.HttpError(
                    response.code(),
                    response.message()
                )
                response.body()?.let { data ->
                    val transformedData = transformResponse(data)
                    saveToCache(transformedData)
                    HttpResult.Success(transformedData)
                } ?: HttpResult.NoData("No data") // TODO: return cache data instead of no data?
            } catch (e: Exception) {
                HttpResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            HttpResult.NetworkError("No network connection")
        }
    }

    suspend fun <T, R> authenticate(
        networkCall: suspend () -> Response<T>,
        saveToken: (T) -> Unit,
        saveToCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (networkHelper.isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) return HttpResult.HttpError(
                    response.code(),
                    response.message()
                )
                response.body()?.let { data ->
                    saveToken(data)
                    val transformedData = transformResponse(data)
                    saveToCache(transformedData)
                    HttpResult.Success(transformedData)
                } ?: HttpResult.NoData("No data")
            } catch (e: Exception) {
                HttpResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            HttpResult.NetworkError("No network connection")
        }
    }

    suspend fun <T, R> updateData(
        networkCall: suspend () -> Response<T>,
        updateCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (networkHelper.isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) HttpResult.HttpError(
                    response.code(),
                    response.message()
                )
                response.body()?.let { data ->
                    val transformedData = transformResponse(data)
                    updateCache(transformedData)
                    HttpResult.Success(transformedData)
                } ?: HttpResult.NoData("No data") // TODO: return cache data instead of no data?
            } catch (e: Exception) {
                HttpResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            HttpResult.NetworkError("No network connection")
        }
    }

    suspend fun <T> deleteData(
        networkCall: suspend () -> Response<T>,
        deleteFromCache: suspend () -> Unit = {}
    ): HttpResult<Unit> {
        return if (networkHelper.isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) HttpResult.HttpError(
                    response.code(),
                    response.message()
                )
                deleteFromCache()
                HttpResult.Success(Unit)
            } catch (e: Exception) {
                HttpResult.NetworkError("Error deleting data: ${e.message}")
            }
        } else {
            HttpResult.NetworkError("No network connection")
        }
    }
}