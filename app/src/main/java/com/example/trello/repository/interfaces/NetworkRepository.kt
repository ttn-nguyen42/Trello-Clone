package com.example.trello.repository.interfaces

import com.example.trello.models.response.CreateUserProfileResponse
import com.example.trello.models.shared.UserInfo
import com.example.trello.network.ApiResult

interface NetworkRepository {
    suspend fun createUserProfile(userInfo: UserInfo): ApiResult<CreateUserProfileResponse>
    suspend fun getUserProfile(uid: String): ApiResult<UserInfo?>
}