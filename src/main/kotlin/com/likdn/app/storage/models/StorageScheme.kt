package com.likdn.app.storage.models

import com.likdn.core.models.MIMEType
import com.likdn.app.customers.models.CustomersScheme
import com.likdn.app.tokens.models.TokensScheme
import com.likdn.utils.UUIDTimedTable

object StorageScheme : UUIDTimedTable("storage") {
    val originFileName = varchar("origin_file_name", 255)
    val fileName = varchar("file_name", 255)
    val fileSize = long("file_size")
    val isPublic = bool("public")
    val group = varchar("group", 100).nullable()
    val shortenFileName = varchar("shorten_file_name", 25).uniqueIndex().nullable()
    val mimeType = enumerationByName<MIMEType>("mimeType", 25)
    val data = text("data").nullable()

    val customerID = reference("customer_id", CustomersScheme.id)
    val tokenID = reference("token_id", TokensScheme.id)
}