package com.amit.aquafill.network

import com.amit.aquafill.network.response.UserResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("users/signing")
    suspend fun signing(
        @Header("Authorization") token: String
    ): UserResponse


    @POST("users/signup")
    suspend fun signup(
        @Header("Authorization") token: String
    ): UserResponse


    @POST("users/reset")
    suspend fun reset(
        @Header("Authorization") token: String
    ): UserResponse
}