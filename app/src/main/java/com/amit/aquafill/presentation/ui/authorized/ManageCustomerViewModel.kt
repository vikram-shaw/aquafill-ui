package com.amit.aquafill.presentation.ui.authorized

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.aquafill.network.model.CustomerDto
import com.amit.aquafill.network.response.CustomerResponse
import com.amit.aquafill.network.util.NetworkResult
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

data class AddOrUpdateCustomerState (
    val id: String? = null,
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val loading: Boolean = false
)

@HiltViewModel
class ManageCustomerViewModel @Inject constructor(
    private val customerRepository: ICustomerRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CustomerUIState(emptyList()))
    private val uiState: StateFlow<CustomerUIState> = _uiState.asStateFlow()

    private val _uiCustomerState = MutableStateFlow(CustomerUIState(emptyList()))
    val uiCustomerState: StateFlow<CustomerUIState> = _uiCustomerState.asStateFlow()

    private val _uiAddOrUpdateCustomerState = MutableStateFlow((AddOrUpdateCustomerState()))
    val uiAddOrUpdateCustomerState: StateFlow<AddOrUpdateCustomerState> = _uiAddOrUpdateCustomerState.asStateFlow()

    fun updateCustomerStateBy(customerName: String) {
        _uiCustomerState.value = _uiState.value.copy(
            customers = uiState.value.customers.filter { customer ->
                customer.name.lowercase().contains(customerName.lowercase())
            }
        )
    }

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
                    _uiState.value = _uiState.value.copy(customers = customers)
                    _uiCustomerState.value = _uiCustomerState.value.copy(customers = customers)
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

    fun deleteCustomer(
        id: String?,
        close: ()-> Unit = {}) {
        if(id != null) {
            viewModelScope.launch {
                when (val response = customerRepository.delete(id)) {
                    is NetworkResult.Success -> {
                        getCustomers()
                        close()
                    }
                    is NetworkResult.Error -> {
                        Log.d("delete error", response.message.toString())
                        close()
                    }
                    is NetworkResult.Loading -> {
                        Log.d("delete loading", response.toString())
                    }
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
            name = name
        )
    }
    fun updateAddress(address: String) {
        _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
            address = address
        )
    }
    fun updatePhone(phone: String) {
        _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
            phone = phone
        )
    }

    fun fillUpdateCustomer(id: String, name: String, address: String, phone: String) {
        _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
            id = id,
            name = name,
            address = address,
            phone = phone
        )
    }

    fun resetForm() {
        _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
            id = null,
            name = "",
            address = "",
            phone = ""
        )
    }

    fun addOrUpdateCustomer(close: () -> Unit) {
        _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
            loading = true
        )
        viewModelScope.launch {
            val response: NetworkResult<CustomerResponse>
            if(_uiAddOrUpdateCustomerState.value.id != null) {
                response = customerRepository.update(
                    id = _uiAddOrUpdateCustomerState.value.id!!,
                    CustomerDto(
                        name = _uiAddOrUpdateCustomerState.value.name,
                        address = _uiAddOrUpdateCustomerState.value.address,
                        phone = _uiAddOrUpdateCustomerState.value.phone.toLong()
                    )
                )
            } else {
                response = customerRepository.add(
                    CustomerDto(
                        name = _uiAddOrUpdateCustomerState.value.name,
                        address = _uiAddOrUpdateCustomerState.value.address,
                        phone = _uiAddOrUpdateCustomerState.value.phone.toLong()
                    )
                )
            }
            when(response) {
                is NetworkResult.Success -> {
                    _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
                        name = "",
                        address = "",
                        phone = "",
                        loading = false
                    )
                    getCustomers()
                    close()
                }
                else -> {
                    _uiAddOrUpdateCustomerState.value = _uiAddOrUpdateCustomerState.value.copy(
                        loading = false
                    )
                }
            }
        }
    }
}