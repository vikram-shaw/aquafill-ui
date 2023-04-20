package com.amit.aquafill.repository.user

import com.amit.aquafill.network.response.UserInfoResponse
import com.amit.aquafill.network.response.UserResponse
import com.amit.aquafill.network.util.NetworkResult
import retrofit2.Response


interface IUserRepository {
    suspend fun signup(email: String, password: String): NetworkResult<UserResponse>
    suspend fun reset(email: String, password: String): NetworkResult<UserResponse>
    suspend fun signing(email: String, password: String): NetworkResult<UserResponse>
    suspend fun user(): NetworkResult<UserInfoResponse>
}