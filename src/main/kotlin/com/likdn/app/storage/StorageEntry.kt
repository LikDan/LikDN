package com.likdn.app.storage

import com.likdn.app.customers.CustomersEntry
import com.likdn.app.tokens.TokensEntry
import com.likdn.app.tokens.TokensScheme
import io.ktor.http.*
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class StorageEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var originFileName by StorageScheme.originFileName
    var fileName by StorageScheme.fileName
    var shortenFileName by StorageScheme.shortenFileName
    var data by StorageScheme.data
    var contentType by StorageScheme.contentType
    var contentSubtype by StorageScheme.contentSubtype
    var customerID by StorageScheme.customerID
    var tokenID by StorageScheme.tokenID

    val customer by CustomersEntry referencedOn StorageScheme.customerID
    val token by TokensEntry referrersOn TokensScheme.customerID

    fun dto(): StorageDTO =
        StorageDTO(originFileName, fileName, shortenFileName, data, customerID.toString(), tokenID.toString())

    fun contentType(): ContentType = ContentType(this.contentType, this.contentSubtype)

    companion object : UUIDEntityClass<StorageEntry>(StorageScheme)
}