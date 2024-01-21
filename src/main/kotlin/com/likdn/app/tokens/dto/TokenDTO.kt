package com.likdn.app.tokens.dto

import com.likdn.core.models.MIMEType
import com.likdn.app.tokens.models.TokenPermissions
import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val id: String,
    val token: String,
    val name: String,
    val expireAt: String?,
    val issuedAt: String,
    val permissions: List<TokenPermissions>,
    val mimeTypes: ArrayList<MIMEType>,
    val groups: ArrayList<String>,
    val customerID: String,
)
