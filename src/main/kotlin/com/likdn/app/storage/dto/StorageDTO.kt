package com.likdn.app.storage.dto

import com.likdn.core.models.MIMEType
import kotlinx.serialization.Serializable

@Serializable
data class StorageDTO(
    val originFileName: String,
    val fileName: String,
    val fileSize: Long,
    val isPublic: Boolean,
    val contentType: List<String>,
    val shortenFileName: String?,
    val mimeType: MIMEType,
    val group: String?,
    val data: String?,
    val customerID: String,
    val tokenID: String
)
