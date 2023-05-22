package com.amit.aquafill.network

import com.amit.aquafill.network.model.EntryDto
import com.amit.aquafill.presentation.ui.authorized.entry.PaymentStatus
import kotlinx.coroutines.flow.Flow
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
    fun entries(
        @Query("customerIds") customerIds: List<String>,
        @Query("startDate") startDate: Date,
        @Query("endDate") endDate: Date,
        @Query("paidStatus") status: PaymentStatus): Flow<List<EntryDto>>

    @POST("entries/create")
    suspend fun add(@Body entryDto: EntryDto)
    @PUT("entries/update/{id}")
    suspend fun update(@Path("id") id: String, @Body entryDto: EntryDto)
    @DELETE("entries/delete/{id}")
    suspend fun delete(@Path("id") id: String)
}