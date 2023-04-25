package com.likdn.app.customers

import com.likdn.app.billings.BillingsEntry
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CustomersEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by CustomersScheme.name
    var balance by CustomersScheme.balance
    var nextPaymentAt by CustomersScheme.nextPaymentAt
    var billingID by CustomersScheme.billingID
    val billing by BillingsEntry referencedOn CustomersScheme.billingID

    companion object : UUIDEntityClass<CustomersEntry>(CustomersScheme)
}