package com.likdn.app.customers.models

import com.likdn.app.billings.models.BillingsScheme
import com.likdn.utils.UUIDTimedTable
import org.jetbrains.exposed.sql.javatime.datetime

object CustomersScheme : UUIDTimedTable("customers") {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val country = varchar("country", 255)
    val balance = float("balance")
    val nextPaymentAt = datetime("next_payment_at")
    val billingID = reference("billing_id", BillingsScheme.id).nullable()
}