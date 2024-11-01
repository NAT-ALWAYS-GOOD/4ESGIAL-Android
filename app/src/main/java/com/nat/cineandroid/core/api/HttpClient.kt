package com.nat.cineandroid.core.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response

abstract class HttpClient(@ApplicationContext private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    protected suspend fun <T, R> fetchData(
        networkCall: suspend () -> Response<T>,
        cacheCall: suspend () -> R?,
        saveToCache: suspend (R) -> Unit,
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (isNetworkAvailable()) {
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

    protected suspend fun <T, R> createData(
        networkCall: suspend () -> Response<T>,
        saveToCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (isNetworkAvailable()) {
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
                } ?: HttpResult.NoData("No data") // TODO: return cache data instead of no data?
            } catch (e: Exception) {
                HttpResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            HttpResult.NetworkError("No network connection")
        }
    }

    protected suspend fun <T, R> updateData(
        networkCall: suspend () -> Response<T>,
        updateCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): HttpResult<R> {
        return if (isNetworkAvailable()) {
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

    protected suspend fun <T> deleteData(
        networkCall: suspend () -> Response<T>,
        deleteFromCache: suspend () -> Unit = {}
    ): HttpResult<Unit> {
        return if (isNetworkAvailable()) {
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