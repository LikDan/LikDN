package com.likdn.app.storage.controllers

import com.likdn.app.billings.models.BillingPeriod
import com.likdn.app.billings.models.BillingsEntry
import com.likdn.app.customers.models.CustomersEntry
import com.likdn.app.plans.models.PlansEntry
import com.likdn.app.storage.dto.CreateStorageDTO
import com.likdn.app.storage.dto.StorageDTO
import com.likdn.app.storage.models.StorageEntry
import com.likdn.app.storage.models.StorageScheme
import com.likdn.app.tokens.models.TokenPermissions
import com.likdn.app.tokens.models.TokensEntry
import com.likdn.core.exceptions.ForbiddenException
import com.likdn.core.exceptions.NotFoundException
import com.likdn.core.exceptions.PaymentRequiredException
import com.likdn.core.exceptions.UnappropriatedContentException
import com.likdn.core.models.MIMEType
import com.likdn.core.permissions.assertPermission
import com.likdn.utils.getByContentType
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime


object StorageController {
    fun create(token: TokensEntry, dto: CreateStorageDTO): StorageDTO {
        token.assertPermission(TokenPermissions.WRITE)

        val extension = dto.file.originalFileName?.substringAfterLast(".")
            ?: throw UnappropriatedContentException("originalFileName")
        val filename = "${generateFileName()}.$extension"

        val customer = transaction { CustomersEntry.findById(token.customerID) }
            ?: throw NotFoundException("customer")

        if (customer.billingID == null) throw UnappropriatedContentException("no billing")

        val billing = transaction { BillingsEntry.findById(customer.billingID!!) }
            ?: throw NotFoundException("billing")

        if (customer.nextPaymentAt <= LocalDateTime.now()) {
            //todo move payments to crone
            if (customer.balance - billing.price < 0) {
                throw PaymentRequiredException("Inefficient founds")
            }

            transaction {
                customer.balance -= billing.price
                when (billing.period) {
                    BillingPeriod.DAILY -> customer.nextPaymentAt.plusDays(1)
                    BillingPeriod.WEEKLY -> customer.nextPaymentAt.plusWeeks(1)
                    BillingPeriod.MONTHLY -> customer.nextPaymentAt.plusMonths(1)
                    BillingPeriod.ANNUALLY -> customer.nextPaymentAt.plusYears(1)
                }
            }
        }

        val plan = transaction { PlansEntry.findById(billing.planID) }
            ?: throw NotFoundException("plan")

        var fileSize = 0L
        var fileAmount = 0L
        if (plan.maxFiles != null || plan.maxStorage != null) {
            transaction {
                val result = StorageScheme
                    .slice(StorageScheme.fileName.count(), StorageScheme.fileSize.sum())
                    .select { StorageScheme.customerID eq customer.id }
                    .first()

                fileAmount = result[StorageScheme.fileName.count()]
                fileSize = result[StorageScheme.fileSize.sum()] ?: 0L
            }

        }

        if (plan.maxFiles != null && fileAmount > plan.maxFiles!!) {
            throw PaymentRequiredException("You have reached file size limit")
        }

        if (plan.maxStorage != null && fileSize > plan.maxStorage!!) {
            throw PaymentRequiredException("You have reached storage limit")
        }

        val shorten = if (plan.shortURL) generateFileName(true) else null
        val origin = dto.file.originalFileName ?: throw UnappropriatedContentException("originalFileName")
        val contentType = dto.file.contentType ?: throw UnappropriatedContentException("contentType")
        val mimeType = MIMEType.getByContentType(contentType)

        if (mimeType !in token.mimeTypes && MIMEType.ALL !in token.mimeTypes) {
            throw ForbiddenException("no permission to save file with mimeType: $mimeType")
        }

        if (token.groups.isNotEmpty() && dto.group != null && dto.group !in token.groups) {
            throw ForbiddenException("no permission to save file in group: ${dto.group}")
        }

        val currentFileSize = customer.file(filename).outputStream().use { fos ->
            dto.file.streamProvider().copyTo(fos)
        }

        val entry = transaction {
            StorageEntry.new {
                this.shortenFileName = shorten
                this.fileName = filename
                this.fileSize = currentFileSize
                this.isPublic = dto.isPublic
                this.group = dto.group
                this.originFileName = origin
                this.mimeType = mimeType
                this.data = dto.data
                this.customerID = customer.id
                this.tokenID = token.id
            }
        }

        return entry.dto()
    }

    fun getEntryByFullFileName(token: TokensEntry?, fileName: String): StorageDTO {
        token?.assertPermission(TokenPermissions.READ_INFO)

        return getDBEntryByFullFileName(token, fileName).dto()
    }

    fun getInfoByShortenFileName(token: TokensEntry?, fileName: String): StorageDTO {
        token?.assertPermission(TokenPermissions.READ_INFO)
        return this.getDBEntryByShortenFileName(token, fileName).dto()
    }

    fun getStreamByFullFileName(token: TokensEntry?, fileName: String): Pair<StorageDTO, InputStream> {
        token?.assertPermission(TokenPermissions.READ)

        val entry = getEntryByFullFileName(token, fileName)
        val stream = entry.file(entry.fileName).inputStream()
        return entry to stream
    }

    fun getStreamByShortFileName(token: TokensEntry?, fileName: String): Pair<StorageDTO, InputStream> {
        token?.assertPermission(TokenPermissions.READ)

        val entry = getDBEntryByShortenFileName(token, fileName).dto()
        val stream = entry.file(entry.fileName).inputStream()
        return entry to stream
    }


    fun getCustomerEntries(token: TokensEntry): List<StorageDTO> {
        token.assertPermission(TokenPermissions.GET_FILE_LIST)

        val storage = transaction {
            StorageEntry.find {
                StorageScheme.customerID eq token.customerID
            }.map { it.dto() }
        }

        return storage
    }

    fun delete(token: TokensEntry, fileName: String): StorageDTO {
        token.assertPermission(TokenPermissions.DELETE)

        val entry = getDBEntryByFullFileName(token, fileName)
        if (entry.mimeType !in token.mimeTypes && MIMEType.ALL !in token.mimeTypes) {
            throw ForbiddenException("no permission to delete file with mimeType: ${entry.mimeType}")
        }

        if (token.groups.isNotEmpty() && entry.group != null && entry.group !in token.groups) {
            throw ForbiddenException("no permission to save file in group: ${entry.group}")
        }

        val customer = transaction { CustomersEntry.findById(token.customerID) }
            ?: throw NotFoundException("customer")

        customer.file(fileName).delete()
        transaction {
            entry.delete()
        }

        return entry.dto()
    }


    private fun getDBEntryByShortenFileName(token: TokensEntry?, fileName: String): StorageEntry {
        return getEntry(token) { StorageScheme.shortenFileName eq fileName }
    }

    private fun getDBEntryByFullFileName(token: TokensEntry?, fileName: String): StorageEntry {
        return getEntry(token) { StorageScheme.fileName eq fileName }
    }

    private fun getEntry(token: TokensEntry?, fileNameQuery: SqlExpressionBuilder.() -> Op<Boolean>): StorageEntry {
        val storage = transaction {
            StorageEntry.find {
                val filerQuery =
                    if (token == null) StorageScheme.isPublic
                    else StorageScheme.customerID eq token.customerID

                return@find fileNameQuery() and filerQuery
            }.firstOrNull()
        } ?: throw NotFoundException()

        return storage
    }

    private fun CustomersEntry.file(filename: String) =
        File("storage/${this.id}/", filename).apply { parentFile.mkdirs() }

    private fun StorageDTO.file(filename: String) =
        File("storage/${this.customerID}/", filename).apply { parentFile.mkdirs() }

    private fun generateFileName(short: Boolean = false): String {
        val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(if (short) 5 else 200) { alphabet.random() }.joinToString("")
    }
}
