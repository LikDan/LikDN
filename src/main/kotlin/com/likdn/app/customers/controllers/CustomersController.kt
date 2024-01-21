package com.likdn.app.customers.controllers

import com.likdn.app.billings.models.BillingsScheme
import com.likdn.app.customers.dto.CreateCustomerDTO
import com.likdn.app.customers.dto.CustomerDTO
import com.likdn.app.customers.models.CustomersEntry
import com.likdn.app.customers.models.CustomersScheme
import com.likdn.app.payments.models.PaymentCustomer
import com.likdn.app.tokens.models.TokenPermissions
import com.likdn.app.tokens.models.TokensEntry
import com.likdn.core.exceptions.NotFoundException
import com.likdn.core.exceptions.UnappropriatedContentException
import com.likdn.core.models.MIMEType
import com.likdn.core.permissions.assertPermission
import com.likdn.utils.randomString
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*


object CustomersController {
    fun createCustomerByPayment(customer: PaymentCustomer): Pair<CustomersEntry, TokensEntry> =
        createCustomerEntry(CreateCustomerDTO(name = customer.name, email = customer.email), customer.country)

    fun createCustomer(dto: CreateCustomerDTO, ip: String): CustomerDTO {
        if (dto.name == "" || dto.email == "") throw UnappropriatedContentException("empty string received")

        val (customer, token) = createCustomerEntry(dto, "")
        return customer.dto(token.dto())
    }

    fun changeBillingPlan(customer: CustomersEntry, billingID: String) {
        val billingUUID = UUID.fromString(billingID)
        transaction {
            customer.billingID = EntityID(billingUUID, BillingsScheme)
        }
    }

    fun deleteCustomer(token: TokensEntry) {
        token.assertPermission(TokenPermissions.ALL)

        transaction {
            CustomersScheme.deleteWhere { id eq token.customerID }
        }
    }

    private fun createCustomerEntry(dto: CreateCustomerDTO, country: String): Pair<CustomersEntry, TokensEntry> {
        val (customerEntry, tokenEntry) = transaction {
            val customer = CustomersEntry.new {
                this.name = dto.name
                this.email = dto.email
                this.country = country
                this.balance = 0F
                this.nextPaymentAt = LocalDateTime.now()
                this.billingID = null
            }

            val token = TokensEntry.new {
                this.token = randomString()
                this.name = "root-token"
                this.expiredAt = null
                this.permissions = arrayListOf(TokenPermissions.ALL)
                this.mimeTypes = arrayListOf(MIMEType.ALL)
                this.groups = arrayListOf()
                this.issuedAt = LocalDateTime.now()
                this.isRoot = true
                this.customerID = customer.id
            }

            return@transaction customer to token
        }

        return customerEntry to tokenEntry
    }

    fun getCustomerByToken(token: TokensEntry): CustomerDTO {
        return transaction { CustomersEntry.findById(token.customerID) }?.dto()
            ?: throw NotFoundException("customer")
    }
}