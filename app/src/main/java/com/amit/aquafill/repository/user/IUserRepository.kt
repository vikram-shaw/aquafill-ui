package com.amit.aquafill.repository.user

import com.amit.aquafill.network.response.UserResponse
import retrofit2.Response


interface IUserRepository {
    suspend fun signup(email: String, password: String): Response<UserResponse>
    suspend fun reset(email: String, password: String): Response<UserResponse>
    suspend fun signing(email: String, password: String): Response<UserResponse>
}