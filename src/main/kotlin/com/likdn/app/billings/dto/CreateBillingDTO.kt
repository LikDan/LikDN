package com.likdn.app.billings.dto

import com.likdn.app.billings.models.BillingPeriod
import kotlinx.serialization.Serializable

@Serializable
data class CreateBillingDTO(
    val period: BillingPeriod,
    val price: Float,
    val isAvailable: Boolean,
    val planID: String
)