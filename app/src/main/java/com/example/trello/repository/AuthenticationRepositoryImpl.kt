package com.example.trello.repository

import com.example.trello.models.shared.UserCredential
import com.example.trello.network.AuthenticationResult
import com.example.trello.repository.interfaces.AuthenticationRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(private val api: FirebaseAuth) :
    AuthenticationRepository {
    override suspend fun register(email: String, password: String): AuthenticationResult {
        try {
            val result: AuthResult = api.createUserWithEmailAndPassword(email, password).await()
            if (result.user == null) {
                return AuthenticationResult.Failure("Registration failed, please try again!")
            }
            return AuthenticationResult.Success(UserCredential(result.user!!.uid, result.user!!.email!!))
        } catch (e: Exception) {
            return AuthenticationResult.Failure(e.message ?: "Registration failed, please try again!")
        }
    }

    override suspend fun login(email: String, password: String): AuthenticationResult {
        try {
            val result: AuthResult = api.signInWithEmailAndPassword(email, password).await()
            if (result.user == null) {
                return AuthenticationResult.Failure("Sign in failed, please try again")
            }
            return AuthenticationResult.Success(UserCredential(result.user!!.uid, result.user!!.email!!))
        } catch (e: Exception) {
            return AuthenticationResult.Failure(e.message ?: "Sign in failed, please try again")
        }
    }

    override fun logout() {
        api.signOut()
    }

    override fun isLoggedIn(): Boolean {
        val currentUser: FirebaseUser? = api.currentUser
        return (currentUser != null)
    }

    override fun getUserId(): String? {
        return (api.currentUser?.uid)
    }
}