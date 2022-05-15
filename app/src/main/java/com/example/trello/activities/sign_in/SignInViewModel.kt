package com.example.trello.activities.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trello.application.interfaces.DispatcherProvider
import com.example.trello.models.response.CreateUserProfileResponse
import com.example.trello.models.shared.UserInfo
import com.example.trello.network.ApiResult
import com.example.trello.network.AuthenticationResult
import com.example.trello.repository.interfaces.AuthenticationRepository
import com.example.trello.repository.interfaces.NetworkRepository
import com.example.trello.utils.interfaces.BaseViewModel
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val networkRepository: NetworkRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<SignInStates>(SignInStates.Initial) {

    var email: String = ""
    var password: String = ""

    fun login() {
        emit(SignInStates.Loading)
        viewModelScope.launch(dispatcherProvider.io) {
            val result: AuthenticationResult = repository.login(email, password)
            result.on({
                val infoResult: ApiResult<UserInfo?> = networkRepository.getUserProfile(it.id)
                infoResult.on({ res: UserInfo? ->
                    if (res == null) {
                        networkRepository.createUserProfile(
                            UserInfo(
                                id = it.id,
                                email = it.email,
                                name = null,
                                fcmToken = null,
                                mobile = null,
                            )
                        )
                        emit(SignInStates.Success)
                    } else {
                        emit(SignInStates.Success)
                    }
                }, {
                    repository.logout()
                    emit(SignInStates.Failure("Not able to sign in right now"))
                })
            }, { message: String ->
                emit(SignInStates.Failure(message))
            })
        }
    }

    fun isSignInDisabled(): Boolean {
        return (email.isEmpty() || password.isEmpty())
    }
}