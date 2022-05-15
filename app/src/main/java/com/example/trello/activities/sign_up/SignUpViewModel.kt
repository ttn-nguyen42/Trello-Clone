package com.example.trello.activities.sign_up

import androidx.lifecycle.viewModelScope
import com.example.trello.application.interfaces.DispatcherProvider
import com.example.trello.models.response.CreateUserProfileResponse
import com.example.trello.models.shared.UserInfo
import com.example.trello.network.ApiResult
import com.example.trello.network.AuthenticationResult
import com.example.trello.repository.interfaces.AuthenticationRepository
import com.example.trello.repository.interfaces.NetworkRepository
import com.example.trello.utils.interfaces.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val networkRepository: NetworkRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<SignUpStates>(SignUpStates.Initial) {

    var name: String = ""
    var email: String = ""
    var password: String = ""
    var retype: String = ""

    fun signUp() {
        emit(SignUpStates.Loading)
        if (!repository.isLoggedIn()) {
            viewModelScope.launch(dispatcherProvider.io) {
                val result: AuthenticationResult = repository.register(email, password)
                result.on({
                    val infoResult: ApiResult<CreateUserProfileResponse> =
                        networkRepository.createUserProfile(
                            UserInfo(
                                id = it.id,
                                name = name,
                                email = it.email,
                                fcmToken = null,
                                mobile = null
                            )
                        )
                    infoResult.on({
                        emit(SignUpStates.Success)
                    }, { message: String ->
                        emit(SignUpStates.Failure(message))
                    })
                }, { message: String ->
                    emit(SignUpStates.Failure(message))
                })
            }
        }
    }

    fun isSignUpDisabled(): Boolean {
        return (name.isEmpty() || name.isEmpty() || email.isEmpty() || !isPasswordValid() || !isPasswordDoubleChecked())
    }

    fun isPasswordValid(): Boolean {
        return (password.isNotEmpty() && password.length > 8)
    }

    private fun isPasswordDoubleChecked(): Boolean {
        return (password == retype)
    }
}