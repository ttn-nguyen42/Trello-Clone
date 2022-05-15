package com.example.trello.activities.sign_in

import com.example.trello.utils.interfaces.BaseState

sealed class SignInStates : BaseState() {
    object Success : SignInStates()
    class Failure(val message: String) : SignInStates()
    object Loading : SignInStates()
    object Initial : SignInStates()

    fun on(
        success: () -> Unit,
        failure: (message: String) -> Unit,
        loading: () -> Unit,
        initial: () -> Unit
    ) {
        when (this) {
            is Success -> {
                success()
            }
            is Failure -> {
                failure(this.message)
            }
            is Loading -> {
                loading()
            }
            is Initial -> {
                initial()
            }
        }
    }
}