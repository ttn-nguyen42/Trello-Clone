package com.example.trello.activities.sign_up

import com.example.trello.utils.interfaces.BaseState

sealed class SignUpStates: BaseState() {
    object Success : SignUpStates()
    class Failure(val message: String) : SignUpStates()
    object Loading : SignUpStates()
    object Initial : SignUpStates()

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