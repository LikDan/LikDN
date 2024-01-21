package com.likdn.app.storage.dto

import io.ktor.http.content.*

data class CreateStorageDTO(
    val file: PartData.FileItem,
    val isPublic: Boolean,
    val group: String?,
    val data: String?,
)
