package com.likdn.app.tokens.models

import com.likdn.core.models.MIMEType
import com.likdn.app.customers.models.CustomersScheme
import com.likdn.utils.UUIDTimedTable
import com.likdn.utils.sql.array.array
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object TokensScheme : UUIDTimedTable("tokens") {
    val token = varchar("token", 64).uniqueIndex()
    val name = varchar("name", 64)
    val wasReveal = bool("revealed").clientDefault { true }
    val expireAt = datetime("expire_at").nullable()
    val issuedAt = datetime("issued_at").clientDefault { LocalDateTime.now() }
    val permissions = enumerationByName<TokenPermissions>("permissions", 25).array()
    val mimeTypes = enumerationByName<MIMEType>("mime_types", 25).array()
    val groups = varchar("groups", 100).array()
    val isRoot = bool("root").clientDefault { false }
    val customerID = reference("customer_id", CustomersScheme.id, onDelete = ReferenceOption.CASCADE)
}