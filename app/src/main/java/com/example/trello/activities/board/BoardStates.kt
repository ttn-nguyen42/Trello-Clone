package com.example.trello.activities.board

import com.example.trello.utils.interfaces.BaseState

sealed class BoardStates: BaseState() {
    object Success : BoardStates()
    class Failure(val message: String) : BoardStates()
    object Loading : BoardStates()
    object Initial : BoardStates()

    fun on(
        success: () -> Unit,
        failure: (message: String) -> Unit,
        loading: () -> Unit,
        initial: () -> Unit,
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