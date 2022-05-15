package com.example.trello.activities.profile

import com.example.trello.activities.board.BoardStates
import com.example.trello.utils.interfaces.BaseState

sealed class ProfileStates : BaseState() {
    object Success : ProfileStates()
    class Failure(val message: String) : ProfileStates()
    object Loading : ProfileStates()
    object Initial : ProfileStates()
    object Finish : ProfileStates()

    fun on(
        success: () -> Unit,
        failure: (message: String) -> Unit,
        loading: () -> Unit,
        initial: () -> Unit,
        finish: () -> Unit,
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
            is Finish -> {
                finish()
            }
        }
    }
}