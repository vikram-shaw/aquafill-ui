package com.amit.aquafill.network.model

import com.google.gson.annotations.SerializedName

data class CustomerDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("phone")
    val phone: Long
)