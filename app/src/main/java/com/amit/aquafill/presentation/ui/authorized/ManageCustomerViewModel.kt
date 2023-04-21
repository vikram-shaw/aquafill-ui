package com.amit.aquafill.presentation.ui.authorized

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.aquafill.network.model.CustomerDto
import com.amit.aquafill.network.response.CustomerResponse
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.repository.customer.CustomerRepository
import com.amit.aquafill.repository.customer.ICustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerUIState (
    val customers: List<CustomerResponse>,
)
@HiltViewModel
class ManageCustomerViewModel @Inject constructor(
    private val customerRepository: ICustomerRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CustomerUIState(emptyList()))
    val uiState: StateFlow<CustomerUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when(val response = customerRepository.customers()){
                is NetworkResult.Success -> {
                    val response = response.data!!
                    _uiState.value = _uiState.value.copy(customers = response)
                    Log.d("customer info", response.toString())
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