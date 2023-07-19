package com.amit.aquafill.network

import com.amit.aquafill.network.model.AddEntryDto
import com.amit.aquafill.network.model.EntryDto
import com.amit.aquafill.network.response.EntryResponse
import com.amit.aquafill.presentation.ui.authorized.entry.PaymentStatus
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface EntryService {
    @GET("entries/get")
    suspend fun entries(
        @Query("customerId") customerId: String,
        @Query("startDate") startDate: Date,
        @Query("endDate") endDate: Date,
        @Query("paidStatus") paidStatus: List<String>
    ): Response<List<EntryResponse>>
    @POST("entries/create")
    suspend fun add(@Body entryDto: AddEntryDto): Response<EntryResponse>
    @PUT("entries/update/{id}")
    suspend fun update(@Path("id") id: String, @Body entryDto: EntryDto)
    @DELETE("entries/delete/{id}")
    suspend fun delete(@Path("id") id: String)
}