package com.likdn.app.payments.models

import kotlinx.serialization.Serializable

@Serializable
data class PaymentCustomer(
    val name: String,
    val email: String,
    val country: String
)