package com.amit.aquafill.network.response

import com.amit.aquafill.presentation.ui.authorized.entry.PaymentStatus
import com.google.gson.annotations.SerializedName
import java.util.Date

data class AddEntryResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("filledBottleGiven") val given: Int,
    @SerializedName("emptyBottleTaken") val taken: Int,
    @SerializedName("remainingBottle") val remaining: Int,
    @SerializedName("date") val date: Date,
    @SerializedName("bottleType") val bottleType: String,
    @SerializedName("perBottleCost") val pricePerBottle: Double,
    @SerializedName("isPaid") val status: PaymentStatus,
    @SerializedName("user") val userId: String,
    @SerializedName("customer") val customer: String
)