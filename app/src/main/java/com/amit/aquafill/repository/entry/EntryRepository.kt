package com.amit.aquafill.repository.entry

import com.amit.aquafill.network.EntryService
import com.amit.aquafill.network.model.EntryDto
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

    override suspend fun add(entryDto: EntryDto) {
        return entryService.add(entryDto)
    }

    override suspend fun update(id: String, entryDto: EntryDto) {
        return entryService.update(id, entryDto)
    }

    override suspend fun delete(id: String) {
        return entryService.delete(id)
    }
}