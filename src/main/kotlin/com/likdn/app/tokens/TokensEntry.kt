package com.likdn.app.tokens

import com.likdn.app.customers.CustomersEntry
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class TokensEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var token by TokensScheme.token
    var expiredAt by TokensScheme.expireAt
    var issuedAt by TokensScheme.issuedAt
    var permissions by TokensScheme.permissions
    var customerID by TokensScheme.customerID
    val customer by CustomersEntry referencedOn TokensScheme.customerID

    fun hasPermission(permission: TokenPermissions) = permission in permissions

    companion object : UUIDEntityClass<TokensEntry>(TokensScheme)
}