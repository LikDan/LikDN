package com.likdn.app.customers.dto

import com.likdn.app.customers.dto.CustomerDTO
import kotlinx.serialization.Serializable

@Serializable
data class CreateCustomerResponseDTO(
    val customer: CustomerDTO,
    val paymentDescription: String,
    val paymentLink: String
)