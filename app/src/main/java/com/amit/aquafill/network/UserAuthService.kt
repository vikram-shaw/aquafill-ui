package com.amit.aquafill.network

import com.amit.aquafill.network.response.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET
interface UserAuthService {
    @GET("users/user")
    suspend fun user(): Response<UserInfoResponse>
}