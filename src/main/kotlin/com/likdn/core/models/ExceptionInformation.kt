package com.likdn.core.models

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionInformation(
    val message: String,
    val status: String,
    val trace: String? = null
)