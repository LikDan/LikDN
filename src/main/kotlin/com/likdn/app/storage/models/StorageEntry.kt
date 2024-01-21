package com.likdn.app.storage.models

import com.likdn.app.customers.models.CustomersEntry
import com.likdn.app.storage.dto.StorageDTO
import com.likdn.app.tokens.models.TokensEntry
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class StorageEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var originFileName by StorageScheme.originFileName
    var fileName by StorageScheme.fileName
    var fileSize by StorageScheme.fileSize
    var isPublic by StorageScheme.isPublic
    var group by StorageScheme.group
    var shortenFileName by StorageScheme.shortenFileName
    var mimeType by StorageScheme.mimeType
    var data by StorageScheme.data
    var customerID by StorageScheme.customerID
    var tokenID by StorageScheme.tokenID

    val customer by CustomersEntry referencedOn StorageScheme.customerID
    val token by TokensEntry referencedOn StorageScheme.tokenID

    fun dto(): StorageDTO =
        StorageDTO(
            originFileName = originFileName,
            fileName = fileName,
            fileSize = fileSize,
            isPublic = isPublic,
            contentType = mimeType.mimeType.toList(),
            shortenFileName = shortenFileName,
            mimeType = mimeType,
            group = group,
            data = data,
            customerID = customerID.toString(),
            tokenID = tokenID.toString()
        )

    companion object : UUIDEntityClass<StorageEntry>(StorageScheme)
}