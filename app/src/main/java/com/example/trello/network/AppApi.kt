package com.example.trello.network

import com.example.trello.models.response.CreateUserProfileResponse
import com.example.trello.models.shared.UserInfo
import retrofit2.Response
import retrofit2.http.*

interface AppApi {
    @PUT("users/{uid}.json")
    suspend fun createUserProfile(@Path("uid") uid: String, @Body info: UserInfo): Response<CreateUserProfileResponse>

    @GET("users/{uid}.json")
    suspend fun getUserProfile(@Path("uid") uid: String): Response<UserInfo?>
}