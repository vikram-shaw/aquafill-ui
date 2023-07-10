package com.amit.aquafill.network.model

import com.amit.aquafill.presentation.ui.authorized.entry.BottleType
import com.amit.aquafill.presentation.ui.authorized.entry.PaymentStatus
import com.google.gson.annotations.SerializedName
import java.util.Date

data class EntryDto(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("filledBottleGiven") val given: Int,
    @SerializedName("emptyBottleTaken") val taken: Int,
    @SerializedName("remainingBottle") val remaining: Int,
    @SerializedName("date") val date: Date,
    @SerializedName("bottleType") val bottleType: String,
    @SerializedName("perBottleCost") val pricePerBottle: Double,
    @SerializedName("isPaid") val status: PaymentStatus,
    @SerializedName("customerId") val customerId: String
)

data class AddEntryDto(
    @SerializedName("filledBottleGiven") val given: Int,
    @SerializedName("emptyBottleTaken") val taken: Int,
    @SerializedName("remainingBottle") val remaining: Int,
    @SerializedName("date") val date: Date,
    @SerializedName("bottleType") val bottleType: String,
    @SerializedName("perBottleCost") val pricePerBottle: Double,
    @SerializedName("isPaid") val status: PaymentStatus,
    @SerializedName("customerId") val customerId: String
)
