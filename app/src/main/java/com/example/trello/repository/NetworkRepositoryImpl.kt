package com.example.trello.repository

import android.util.Log
import com.example.trello.models.response.CreateUserProfileResponse
import com.example.trello.models.shared.UserInfo
import com.example.trello.network.ApiResult
import com.example.trello.network.AppApi
import com.example.trello.repository.interfaces.NetworkRepository
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(private val api: AppApi) : NetworkRepository {
    override suspend fun createUserProfile(userInfo: UserInfo): ApiResult<CreateUserProfileResponse> {
        return try {
            Log.e("NETWORK", userInfo.id)
            val response: Response<CreateUserProfileResponse> =
                api.createUserProfile(userInfo.id, userInfo)
            val body: CreateUserProfileResponse? = response.body()
            if (response.isSuccessful && body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Failure(response.message())
            }
        } catch (e: Exception) {
            ApiResult.Failure(e.message ?: "Create user profile failed. Please try again")
        }
    }

    override suspend fun getUserProfile(uid: String): ApiResult<UserInfo?> {
        return try {
            val response: Response<UserInfo?> = api.getUserProfile(uid)
            val body: UserInfo? = response.body()
            if (response.isSuccessful) {
                ApiResult.Success(body)
            } else {
                ApiResult.Failure(response.message())
            }
        } catch (e: Exception) {
            ApiResult.Failure(e.message ?: "Get user profile failed. Please try again")
        }
    }
}