package com.likdn.app.plans.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlanDTO(
    val id: String,
    val name: String,
    val maxStorage: Long?,
    val maxFiles: Long?,
    val backup: Boolean,
    val shortURL: Boolean,
)