package com.likdn.app.billings.dto

import com.likdn.app.billings.models.BillingPeriod
import kotlinx.serialization.Serializable

@Serializable
data class BillingDTO(
    val id: String,
    val period: BillingPeriod,
    val price: Float,
    val planID: String
)