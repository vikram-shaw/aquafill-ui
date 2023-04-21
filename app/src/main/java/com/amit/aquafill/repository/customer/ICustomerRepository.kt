package com.amit.aquafill.repository.customer

import com.amit.aquafill.network.response.CustomerResponse
import com.amit.aquafill.network.util.NetworkResult

interface ICustomerRepository {
    suspend fun customers(): NetworkResult<List<CustomerResponse>>
}