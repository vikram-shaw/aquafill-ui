package com.amit.aquafill.network.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String
)
