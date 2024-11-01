package com.nat.cineandroid.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response

abstract class BaseRepository(@ApplicationContext private val context: Context) {

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
    ): ApiResult<R> {
        return if (isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) ApiResult.HttpError(response.code(), response.message())
                response.body()?.let { data ->
                    val transformedData = transformResponse(data)
                    saveToCache(transformedData)
                    ApiResult.Success(transformedData)
                } ?: ApiResult.NoData("No data")
            } catch (e: Exception) {
                val dataFromCache = cacheCall()
                dataFromCache?.let { ApiResult.Success(it) }
                    ?: ApiResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            val dataFromCache = cacheCall()
            dataFromCache?.let { ApiResult.Success(it) }
                ?: ApiResult.NetworkError("No network connection and no data in cache")
        }
    }

    protected suspend fun <T, R> createData(
        networkCall: suspend () -> Response<T>,
        saveToCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): ApiResult<R> {
        return if (isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) ApiResult.HttpError(response.code(), response.message())
                response.body()?.let { data ->
                    val transformedData = transformResponse(data)
                    saveToCache(transformedData)
                    ApiResult.Success(transformedData)
                } ?: ApiResult.NoData("No data") // TODO: return cache data instead of no data?
            } catch (e: Exception) {
                ApiResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            ApiResult.NetworkError("No network connection")
        }
    }

    protected suspend fun <T, R> updateData(
        networkCall: suspend () -> Response<T>,
        updateCache: suspend (R) -> Unit = {},
        transformResponse: (T) -> R
    ): ApiResult<R> {
        return if (isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) ApiResult.HttpError(response.code(), response.message())
                response.body()?.let { data ->
                    val transformedData = transformResponse(data)
                    updateCache(transformedData)
                    ApiResult.Success(transformedData)
                } ?: ApiResult.NoData("No data") // TODO: return cache data instead of no data?
            } catch (e: Exception) {
                ApiResult.NetworkError("Error fetching data: ${e.message}")
            }
        } else {
            ApiResult.NetworkError("No network connection")
        }
    }

    protected suspend fun <T> deleteData(
        networkCall: suspend () -> Response<T>,
        deleteFromCache: suspend () -> Unit = {}
    ): ApiResult<Unit> {
        return if (isNetworkAvailable()) {
            try {
                val response = networkCall()
                if (!response.isSuccessful) ApiResult.HttpError(response.code(), response.message())
                deleteFromCache()
                ApiResult.Success(Unit)
            } catch (e: Exception) {
                ApiResult.NetworkError("Error deleting data: ${e.message}")
            }
        } else {
            ApiResult.NetworkError("No network connection")
        }
    }
}