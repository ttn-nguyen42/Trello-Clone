package com.example.trello.models.shared

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("fcmToken")
    val fcmToken: String?,
    @SerializedName("mobile")
    val mobile: String?,
)
