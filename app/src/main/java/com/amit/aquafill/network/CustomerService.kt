package com.amit.aquafill.network

import com.amit.aquafill.network.response.CustomerResponse
import retrofit2.Response
import retrofit2.http.GET

interface CustomerService {
    @GET("customers/get")
    suspend fun customers(): Response<List<CustomerResponse>>
}