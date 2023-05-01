package com.amit.aquafill.repository.customer

import com.amit.aquafill.network.model.CustomerDto
import com.amit.aquafill.network.response.CustomerResponse
import com.amit.aquafill.network.util.NetworkResult

interface ICustomerRepository {
    suspend fun customers(): NetworkResult<List<CustomerResponse>>
    suspend fun add(customer: CustomerDto): NetworkResult<CustomerResponse>
    suspend fun update(id: String, customer: CustomerDto): NetworkResult<CustomerResponse>
    suspend fun delete(customerId: String): NetworkResult<CustomerResponse>
}