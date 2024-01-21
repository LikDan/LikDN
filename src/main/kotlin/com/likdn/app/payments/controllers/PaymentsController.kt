package com.likdn.app.payments.controllers

import com.likdn.app.billings.models.BillingPeriod
import com.likdn.app.notifications.models.Notification
import com.likdn.app.notifications.models.NotificationType
import com.likdn.app.payments.models.Payment
import com.likdn.app.payments.models.PaymentCustomer
import com.likdn.app.billings.models.BillingsEntry
import com.likdn.app.customers.controllers.CustomersController
import com.likdn.app.customers.models.CustomersEntry
import com.likdn.app.customers.models.CustomersScheme
import com.likdn.app.notifications.controllers.Notifications
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

object PaymentsController {
    fun proceedPayment(payment: Payment) {
        transaction {
            val customer =
                CustomersEntry.find { CustomersScheme.email eq payment.customer.email }
                    .firstOrNull()?.apply { this.country = payment.customer.country }
                    ?: createCustomer(payment.customer)

            val billing = BillingsEntry.findById(UUID.fromString(payment.billingID))!!

            customer.billingID = billing.id

            //todo add billing.periodMs -> indicate period durability in ms
            val now = LocalDateTime.now()
            val nextPaymentAt = when (billing.period) {
                BillingPeriod.DAILY -> now.plusDays(1)
                BillingPeriod.WEEKLY -> now.plusWeeks(1)
                BillingPeriod.MONTHLY -> now.plusMonths(1)
                BillingPeriod.ANNUALLY -> now.plusYears(1)
            }

            customer.nextPaymentAt = nextPaymentAt
        }
    }

    private fun createCustomer(customer: PaymentCustomer): CustomersEntry {
        return CustomersController.createCustomerByPayment(customer).apply {
            val notification = Notification(
                type = NotificationType.EMAIL,
                contact = this.first.email,
                vars = mapOf("token" to this.second.token),
                templateID = "payment_registration_token"
            )
            Notifications.send(notification)
        }.first
    }
}
