package com.example.trello.models.response

import com.google.gson.annotations.SerializedName

data class CreateUserProfileResponse(
    @SerializedName("name")
    val name: String,
)
