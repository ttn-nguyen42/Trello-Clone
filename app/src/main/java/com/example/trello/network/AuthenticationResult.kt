package com.example.trello.network

import com.example.trello.models.shared.UserCredential

sealed class AuthenticationResult private constructor() {
    class Success(val user: UserCredential) : AuthenticationResult()
    class Failure(val message: String) : AuthenticationResult()

    suspend fun on(success: suspend (user: UserCredential) -> Unit, failure: suspend (message: String) -> Unit) {
        when (this) {
            is Success -> {
                success(this.user)
            }
            is Failure -> {
                failure(this.message)
            }
        }
    }
}