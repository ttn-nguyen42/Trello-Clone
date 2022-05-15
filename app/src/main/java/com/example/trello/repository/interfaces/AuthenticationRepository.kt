package com.example.trello.repository.interfaces

import com.example.trello.network.AuthenticationResult

interface AuthenticationRepository {
    suspend fun register(email: String, password: String): AuthenticationResult
    suspend fun login(email: String, password: String): AuthenticationResult
    fun logout()
    fun isLoggedIn(): Boolean
    fun getUserId(): String?
}