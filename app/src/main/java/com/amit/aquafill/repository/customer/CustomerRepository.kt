package com.amit.aquafill.repository.customer

import com.amit.aquafill.network.CustomerService
import com.amit.aquafill.network.response.CustomerResponse
import com.amit.aquafill.network.util.NetworkResult
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerService: CustomerService
): ICustomerRepository {
    override suspend fun customers(): NetworkResult<List<CustomerResponse>> {
        return try {
            val response = customerService.customers()
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong...")
            }
        } catch (e: Exception) {
            print(e.message)
            NetworkResult.Error("Please try after sometime...")
        }
    }

}