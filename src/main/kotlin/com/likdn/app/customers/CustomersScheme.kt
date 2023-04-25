package com.likdn.app.customers

import com.likdn.app.billings.BillingsScheme
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object CustomersScheme : UUIDTable("customers") {
    val name = varchar("name", 255)
    val balance = float("balance")
    val nextPaymentAt = datetime("next_payment_at")
    val billingID = reference("payment_period", BillingsScheme.id)
}