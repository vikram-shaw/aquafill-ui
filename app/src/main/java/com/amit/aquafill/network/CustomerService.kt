package com.amit.aquafill.network

import com.amit.aquafill.network.model.CustomerDto
import com.amit.aquafill.network.response.CustomerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerService {
    @GET("customers/get")
    suspend fun customers(): Response<List<CustomerResponse>>
    @POST("customers/create")
    suspend fun add(@Body customer: CustomerDto): Response<CustomerResponse>
    @PUT("customers/update/{id}")
    suspend fun update(@Path("id") id: String, @Body customer: CustomerDto): Response<CustomerResponse>
    @DELETE("customers/delete/{id}")
    suspend fun delete(@Path("id") id: String): Response<CustomerResponse>
}