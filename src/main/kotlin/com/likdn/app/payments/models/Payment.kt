package com.likdn.app.payments.models

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val event: PaymentEvent,
    val customer: PaymentCustomer,
    val billingID: String
)