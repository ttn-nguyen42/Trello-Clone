package com.example.trello.activities.main

import androidx.lifecycle.ViewModel
import com.example.trello.application.interfaces.DispatcherProvider
import com.example.trello.repository.interfaces.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel(){

    fun isLoggedIn(): Boolean {
        return repository.isLoggedIn()
    }
}