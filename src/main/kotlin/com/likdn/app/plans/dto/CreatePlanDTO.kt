package com.likdn.app.plans.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlanDTO(
    var name: String,
    var maxStorage: Long?,
    var maxFiles: Long?,
    var backup: Boolean,
    var shortURL: Boolean,
)