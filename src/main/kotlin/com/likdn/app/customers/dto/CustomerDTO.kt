package com.likdn.app.customers.dto

import com.likdn.app.tokens.dto.TokenDTO
import kotlinx.serialization.Serializable

@Serializable
data class CustomerDTO(
    val id: String,
    val name: String,
    val balance: Float,
    val nextPaymentAt: String,
    val billingID: String,
    val token: TokenDTO?
)