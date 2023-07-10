package com.amit.aquafill.repository.entry

import com.amit.aquafill.network.model.AddEntryDto
import com.amit.aquafill.network.model.EntryDto
import com.amit.aquafill.network.response.EntryResponse
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.presentation.ui.authorized.entry.PaymentStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface IEntryRepository {
    fun entries(
        customerIds: List<String>,
        startDate: Date,
        endDate: Date,
        status: PaymentStatus
    ): Flow<List<EntryDto>>
    suspend fun add(entryDto: AddEntryDto): NetworkResult<EntryResponse>
    suspend fun update(id: String, entryDto: EntryDto)
    suspend fun delete(id: String)
}