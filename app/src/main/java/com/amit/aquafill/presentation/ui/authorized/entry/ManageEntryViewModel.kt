package com.amit.aquafill.presentation.ui.authorized.entry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.repository.customer.ICustomerRepository
import com.amit.aquafill.repository.entry.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Option<T>(val key: T, val text: String, var isSelected: Boolean = false)

data class EntryUIState(
    val customers: List<Option<String>>
)

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: ICustomerRepository,
    private val entryRepository: EntryRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(EntryUIState(emptyList()))
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
}