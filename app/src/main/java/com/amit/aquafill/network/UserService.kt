package com.amit.aquafill.network

import com.amit.aquafill.domain.model.User
import com.amit.aquafill.network.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users/signing")
    suspend fun signing(
        @Body user: User
    ): UserResponse


    @POST("users/signup")
    suspend fun signup(
        @Body user: User
    ): UserResponse


    @POST("users/reset")
    suspend fun reset(
        @Body user: User
    ): UserResponse
}