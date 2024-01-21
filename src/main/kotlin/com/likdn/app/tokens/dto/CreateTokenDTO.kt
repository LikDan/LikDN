package com.likdn.app.tokens.dto

import com.likdn.core.models.MIMEType
import com.likdn.app.tokens.models.TokenPermissions
import kotlinx.serialization.Serializable

@Serializable
data class CreateTokenDTO(
    val name: String,
    val expireAt: String?,
    val permissions: ArrayList<TokenPermissions>,
    val mimeTypes: ArrayList<MIMEType>,
    val groups: ArrayList<String>,
)
