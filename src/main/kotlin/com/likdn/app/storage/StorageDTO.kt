package com.likdn.app.storage

import kotlinx.serialization.Serializable

@Serializable
data class StorageDTO(
    val originFileName: String,
    val fileName: String,
    val shortenFileName: String?,
    val data: String?,
    val customerID: String,
    val tokenID: String
)
