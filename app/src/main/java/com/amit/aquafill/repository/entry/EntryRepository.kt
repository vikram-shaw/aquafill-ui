package com.amit.aquafill.repository.entry

import android.util.Log
import com.amit.aquafill.network.EntryService
import com.amit.aquafill.network.model.AddEntryDto
import com.amit.aquafill.network.model.EntryDto
import com.amit.aquafill.network.response.EntryResponse
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.presentation.ui.authorized.entry.PaymentStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class EntryRepository @Inject() constructor(private val entryService: EntryService): IEntryRepository {
    override fun entries(
        customerIds: List<String>,
        startDate: Date,
        endDate: Date,
        status: PaymentStatus
    ): Flow<List<EntryDto>> {
        return entryService.entries(customerIds, startDate, endDate, status)
    }

    override suspend fun add(entryDto: AddEntryDto): NetworkResult<EntryResponse> {
        return try {
            val response = entryService.add(entryDto)
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong...")
            }
        } catch (e: Exception) {
            Log.d("entry error",e.message.toString())
            NetworkResult.Error("Please try after sometime...")
        }
    }

    override suspend fun update(id: String, entryDto: EntryDto) {
        return entryService.update(id, entryDto)
    }

    override suspend fun delete(id: String) {
        return entryService.delete(id)
    }
}