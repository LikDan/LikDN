package com.likdn.app.customers.models

import com.likdn.app.billings.models.BillingsEntry
import com.likdn.app.customers.dto.CustomerDTO
import com.likdn.app.tokens.dto.TokenDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CustomersEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by CustomersScheme.name
    var email by CustomersScheme.email
    var country by CustomersScheme.country
    var balance by CustomersScheme.balance
    var nextPaymentAt by CustomersScheme.nextPaymentAt
    var billingID by CustomersScheme.billingID
    val billing by BillingsEntry optionalReferencedOn CustomersScheme.billingID

    fun dto(token: TokenDTO? = null): CustomerDTO =
        CustomerDTO(
            id = this.id.value.toString(),
            name = this.name,
            balance = this.balance,
            nextPaymentAt = this.nextPaymentAt.toString(),
            billingID = this.billingID?.value.toString(),
            token = token
        )

    companion object : UUIDEntityClass<CustomersEntry>(CustomersScheme)
}