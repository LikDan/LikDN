package com.likdn.app.billings.dto

import com.likdn.app.billings.models.BillingPeriod
import kotlinx.serialization.Serializable

@Serializable
data class BillingWithPlanDTO(
    val billingID: String,
    val planID: String,
    val name: String,
    val period: BillingPeriod,
    val maxFiles: Long?,
    val maxStorage: String,
    val price: String,
    val shortURL: Boolean,
)