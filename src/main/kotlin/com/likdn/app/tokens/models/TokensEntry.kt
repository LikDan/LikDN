package com.likdn.app.tokens.models

import com.likdn.app.customers.models.CustomersEntry
import com.likdn.app.tokens.dto.TokenDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class TokensEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var token by TokensScheme.token
    var name by TokensScheme.name
    var expiredAt by TokensScheme.expireAt
    var wasReveal by TokensScheme.wasReveal
    var issuedAt by TokensScheme.issuedAt
    var permissions by TokensScheme.permissions
    var mimeTypes by TokensScheme.mimeTypes
    var groups by TokensScheme.groups
    var isRoot by TokensScheme.isRoot
    var customerID by TokensScheme.customerID
    val customer by CustomersEntry referencedOn TokensScheme.customerID

    fun hasPermission(permission: TokenPermissions) = permission in permissions

    fun dto(): TokenDTO =
        TokenDTO(
            id = this.id.value.toString(),
            token = this.token,
            name = this.name,
            expireAt = this.expiredAt?.toString(),
            issuedAt = this.issuedAt.toString(),
            permissions = this.permissions,
            mimeTypes = this.mimeTypes,
            groups = this.groups,
            customerID = this.customerID.value.toString(),
        )

    fun credentialDTO(): TokenDTO =
        TokenDTO(
            id = this.id.value.toString(),
            token = "<< hidden >>",
            name = this.name,
            expireAt = this.expiredAt?.toString(),
            issuedAt = this.issuedAt.toString(),
            permissions = this.permissions,
            mimeTypes = this.mimeTypes,
            groups = this.groups,
            customerID = this.customerID.value.toString(),
        )

    companion object : UUIDEntityClass<TokensEntry>(TokensScheme)
}