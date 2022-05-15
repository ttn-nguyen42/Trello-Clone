package com.example.trello.utils.interfaces

import android.os.Bundle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S: BaseState>(initial: S): ViewModel() {
    val _flow: MutableStateFlow<S> = MutableStateFlow(initial)
    val flow: StateFlow<S> = _flow

    fun emit(state: S) {
        _flow.value = state
    }
}