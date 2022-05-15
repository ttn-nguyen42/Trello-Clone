package com.example.trello.activities.board

import androidx.lifecycle.viewModelScope
import com.example.trello.application.interfaces.DispatcherProvider
import com.example.trello.models.shared.UserInfo
import com.example.trello.network.ApiResult
import com.example.trello.repository.interfaces.AuthenticationRepository
import com.example.trello.repository.interfaces.NetworkRepository
import com.example.trello.utils.interfaces.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private var authenticationRepository: AuthenticationRepository,
    private var repository: NetworkRepository,
    private var dispatcherProvider: DispatcherProvider
) : BaseViewModel<BoardStates>(BoardStates.Initial) {

    var userInfo: UserInfo? = null

    fun init() {
        getUserInfo()
    }

    fun signOut() {
        authenticationRepository.logout()
    }

    private fun getUserInfo() {
        emit(BoardStates.Loading)
        viewModelScope.launch(dispatcherProvider.io) {
            val result: ApiResult<UserInfo?> =
                repository.getUserProfile(authenticationRepository.getUserId()!!)
            result.on(
                {
                    userInfo = it
                    emit(BoardStates.Success)
                }, {
                    emit(BoardStates.Failure(it))
                }
            )
        }
    }
}