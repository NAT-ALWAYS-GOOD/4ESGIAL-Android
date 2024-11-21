package com.nat.cineandroid.core.api

sealed class HttpResult<out T> {
    data class Success<out T>(val data: T) : HttpResult<T>()
    data class HttpError(val code: Int, val message: String) : HttpResult<Nothing>()
    data class NetworkError(val message: String) : HttpResult<Nothing>()
    data class NoData(val message: String) : HttpResult<Nothing>()
}