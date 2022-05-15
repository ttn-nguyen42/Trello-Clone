package com.example.trello.activities.profile

import androidx.lifecycle.viewModelScope
import com.example.trello.application.interfaces.DispatcherProvider
import com.example.trello.models.response.CreateUserProfileResponse
import com.example.trello.models.shared.UserInfo
import com.example.trello.network.ApiResult
import com.example.trello.repository.interfaces.AuthenticationRepository
import com.example.trello.repository.interfaces.NetworkRepository
import com.example.trello.utils.interfaces.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val repository: NetworkRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ProfileStates>(ProfileStates.Initial) {

    var name: String = ""
    var number: String = ""

    var userInfo: UserInfo? = null

    fun init() {
        getUserInfo()
    }

    private fun getUserInfo() {
        emit(ProfileStates.Loading)
        viewModelScope.launch(dispatcherProvider.io) {
            val userId: String = authenticationRepository.getUserId()!!
            val result: ApiResult<UserInfo?> = repository.getUserProfile(userId)
            result.on(
                {
                    userInfo = it
                    emit(ProfileStates.Success)
                }, {
                    emit(ProfileStates.Failure(it))
                }
            )
        }
    }

    fun updateUserInfo() {
        emit(ProfileStates.Loading)
        viewModelScope.launch(dispatcherProvider.io) {
            val userInfo = UserInfo(userInfo!!.id, userInfo!!.email, name, null, number)
            val result: ApiResult<CreateUserProfileResponse> =
                repository.createUserProfile(userInfo)
            result.on({
                emit(ProfileStates.Finish)
            }, {
                emit(ProfileStates.Failure(it))
            })
        }
    }

    fun isButtonEnabled(): Boolean {
        if ((name.isEmpty() && userInfo?.name == null) || (number.isEmpty() && userInfo?.mobile == null)) {
            return false
        }
        if (name == userInfo?.name && number == userInfo?.mobile) {
            return false
        }
        return true
    }
}