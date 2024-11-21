package com.example.prueba02_raquelmelo.network

import com.example.prueba02_raquelmelo.domain.util.ApiResult
import retrofit2.Response

interface Error

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
}

inline fun <reified T> safeCall(
    execute: () -> Response<T>
): ApiResult<T, DataError.Network> {
    return try {
        val response = execute()

        when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error(DataError.Network.UNKNOWN)
                }
            }

            else -> {
                when (response.code()) {
                    401 -> ApiResult.Error(DataError.Network.UNAUTHORIZED)
                    408 -> ApiResult.Error(DataError.Network.REQUEST_TIMEOUT)
                    409 -> ApiResult.Error(DataError.Network.CONFLICT)
                    413 -> ApiResult.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                    429 -> ApiResult.Error(DataError.Network.TOO_MANY_REQUESTS)
                    in 500..599 -> ApiResult.Error(DataError.Network.SERVER_ERROR)
                    else -> ApiResult.Error(DataError.Network.UNKNOWN)
                }
            }
        }
    } catch (e: Exception) {
        ApiResult.Error(DataError.Network.UNKNOWN)
    }
}