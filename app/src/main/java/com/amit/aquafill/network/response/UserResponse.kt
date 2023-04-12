package com.amit.aquafill.network.response

import com.amit.aquafill.network.model.UserDto
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("user")
    var user: UserDto,

    @SerializedName("token")
    var token: String
)
