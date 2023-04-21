package com.amit.aquafill.network.response

import com.amit.aquafill.network.model.CustomerDto
import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("phone")
    val phone: Long,
)