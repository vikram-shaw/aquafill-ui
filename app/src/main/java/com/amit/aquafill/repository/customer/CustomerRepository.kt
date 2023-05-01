package com.amit.aquafill.repository.customer

import android.util.Log
import com.amit.aquafill.network.CustomerService
import com.amit.aquafill.network.model.CustomerDto
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

    override suspend fun add(customer: CustomerDto): NetworkResult<CustomerResponse> {
        return try {
            val response = customerService.add(customer)
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong")
            }
        }catch (e: Exception) {
            NetworkResult.Error("Something went wrong")
        }
    }

    override suspend fun update(id: String, customer: CustomerDto): NetworkResult<CustomerResponse> {
        return try {
            val response = customerService.update(id, customer)
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Something went wrong")
        }
    }

    override suspend fun delete(customerId: String): NetworkResult<CustomerResponse> {
        return try {
            val response = customerService.delete(customerId)
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong")
            }
        } catch(e: Exception) {
            NetworkResult.Error("Something went wrong")
        }
    }
}