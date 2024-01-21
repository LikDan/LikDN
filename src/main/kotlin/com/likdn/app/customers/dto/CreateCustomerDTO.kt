package com.likdn.app.customers.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCustomerDTO(
    val name: String,
    val email: String,
)