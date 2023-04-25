package com.likdn.app.storage

import com.likdn.app.customers.CustomersScheme
import com.likdn.app.tokens.TokensScheme
import org.jetbrains.exposed.dao.id.UUIDTable

object StorageScheme : UUIDTable("storage") {
    val originFileName = varchar("origin_file_name", 255)
    val fileName = varchar("file_name", 255)
    val shortenFileName = varchar("shorten_file_name", 25).uniqueIndex().nullable()
    val contentType = varchar("content_type", 25)
    val contentSubtype = varchar("content_subtype", 25)
    val data = text("data").nullable()

    val customerID = reference("customer_id", CustomersScheme.id)
    val tokenID = reference("token_id", TokensScheme.id).nullable()
}