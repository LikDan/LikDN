package com.likdn.app.tokens

import com.likdn.app.customers.CustomersScheme
import com.likdn.utils.enumerationArray
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object TokensScheme : UUIDTable("tokens") {
    val token = varchar("token", 64).uniqueIndex()
    val expireAt = datetime("expire_at").nullable()
    val issuedAt = datetime("issued_at").clientDefault { LocalDateTime.now() }
    val permissions = enumerationArray<TokenPermissions>("permissions")
    val customerID = reference("customer_id", CustomersScheme.id)
}