package com.example.trello.network

sealed class ApiResult<T> private constructor() {
    class Success<T>(val data: T): ApiResult<T>()
    class Failure<T>(val message: String): ApiResult<T>()

    suspend fun on(success: suspend (data: T) -> Unit, failure: suspend (message: String) -> Unit) {
        when (this) {
            is Success -> {
                success(this.data)
            }
            is Failure -> {
                failure(this.message)
            }
        }
    }
}