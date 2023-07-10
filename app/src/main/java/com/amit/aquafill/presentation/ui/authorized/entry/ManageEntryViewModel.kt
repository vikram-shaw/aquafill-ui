package com.amit.aquafill.presentation.ui.authorized.entry

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.aquafill.network.model.AddEntryDto
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.repository.customer.ICustomerRepository
import com.amit.aquafill.repository.entry.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class Option<T>(val key: T, val text: String, var isSelected: Boolean = false)

data class EntryUIState(
    val customers: List<Option<String>>,
    var selectedCustomerId: String,
    val givenBottle: String,
    val takenBottle: String,
    val remainingBottle: String,
    val date: Date,
    val bottleType: String,
    val perBottleCost: String,
    val status: PaymentStatus,
    val isAddEntry: Boolean
    )

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: ICustomerRepository,
    private val entryRepository: EntryRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(EntryUIState(emptyList(),
        "",
        "",
        "",
        "",
        Date(),
        BottleType.Normal,
        "",
        PaymentStatus.Paid,
        isAddEntry = false
    ))
    val uiState = _uiState.asStateFlow()

    init {
        getCustomers()
    }

    private fun getCustomers() {
        viewModelScope.launch {
            when(val response = customerRepository.customers()){
                is NetworkResult.Success -> {
                    val customers = response.data!!.sortedBy {
                        it.name
                    }
                    _uiState.value = _uiState.value.copy(customers = customers.map { customer ->
                        Option(key = customer.id, text = customer.name)
                    })
                    Log.d("entry customer info", response.toString())
                }
                is NetworkResult.Error -> {
                    Log.d("customer info", response.message.toString())
                }
                is NetworkResult.Loading -> {
                    Log.d("customer info", response.toString())
                }
            }
        }
    }

    fun addEntry() {
        viewModelScope.launch {
            val entry = AddEntryDto(
                given = uiState.value.givenBottle.toInt(),
                taken = uiState.value.takenBottle.toInt(),
                remaining = uiState.value.remainingBottle.toInt(),
                date = uiState.value.date,
                bottleType = uiState.value.bottleType,
                pricePerBottle = uiState.value.perBottleCost.toDouble(),
                status = uiState.value.status,
                customerId = uiState.value.selectedCustomerId
            )
            when(val response = entryRepository.add(entry)) {
                is NetworkResult.Success -> {
                    Log.d("entry success", response.data.toString())
                    updateIsAddEntry(false)
                }
                is NetworkResult.Error -> {
                    Log.e("entry", response.message.toString())
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    fun updateIsAddEntry(isEntry: Boolean) {
        _uiState.value = _uiState.value.copy(
            isAddEntry = isEntry
        )
    }

    fun updateGivenBottle(bottle: String) {
        _uiState.value = _uiState.value.copy(givenBottle = bottle)
    }
    fun updateTakenBottle(bottle: String) {
        _uiState.value = _uiState.value.copy(takenBottle = bottle)
    }
    fun updateRemainingBottle(bottle: String) {
        _uiState.value = _uiState.value.copy(remainingBottle = bottle)
    }

    fun updateDate(date: Date) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun updateBottleType(bottleType: String) {
        _uiState.value = _uiState.value.copy(bottleType = bottleType)
    }

    fun updatePaymentStatus(paymentStatus: PaymentStatus) {
        _uiState.value = _uiState.value.copy(status = paymentStatus)
    }

    fun updatePerBottleCost(cost: String) {
        _uiState.value = _uiState.value.copy(perBottleCost = cost)
    }
}