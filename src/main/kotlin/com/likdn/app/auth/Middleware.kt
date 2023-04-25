package com.likdn.app.auth

import com.likdn.app.billings.BillingPeriod
import com.likdn.app.billings.BillingsEntry
import com.likdn.app.customers.CustomersEntry
import com.likdn.app.plans.PlansEntry
import com.likdn.app.storage.dir
import com.likdn.app.tokens.TokenPermissions
import com.likdn.app.tokens.TokensEntry
import com.likdn.app.tokens.TokensScheme
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.time.LocalDateTime

val AuthPlugin = createRouteScopedPlugin(name = "RequestLoggingPlugin") {
    onCall { call ->
        val auth = call.request.header("Authorization") ?: ""
        val token = transaction { TokensEntry.find { TokensScheme.token eq auth }.firstOrNull() }
        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@onCall
        }

        val customer = transaction { token.customer }
        val billing = transaction { customer.billing }

        if (customer.nextPaymentAt > LocalDateTime.now()) {
            if (customer.balance - billing.price < 0) {
                call.respond(HttpStatusCode.PaymentRequired, "Inefficient founds")
                return@onCall
            }

            transaction {
                customer.balance -= billing.price
                when (billing.period) {
                    BillingPeriod.WEEKLY -> customer.nextPaymentAt.plusWeeks(1)
                    BillingPeriod.MONTHLY -> customer.nextPaymentAt.plusMonths(1)
                    BillingPeriod.ANNUALLY -> customer.nextPaymentAt.plusYears(1)
                }
            }
        }

        val plan = transaction { billing.plan }

        val fileAmount = customer.dir().length()
        val fileSize = Files.size(customer.dir().toPath())

        if (plan.maxFiles != null && fileAmount > plan.maxFiles!!) {
            call.respond(HttpStatusCode.Forbidden, "You have reached file limit")
            return@onCall
        }

        if (plan.maxStorage != null && fileSize > plan.maxStorage!!) {
            call.respond(HttpStatusCode.Forbidden, "You have reached storage limit")
            return@onCall
        }

        call.attributes.put(AttributeKey("token"), token)
        call.attributes.put(AttributeKey("customer"), customer)
        call.attributes.put(AttributeKey("billing"), billing)
        call.attributes.put(AttributeKey("plan"), plan)
    }
}

fun ApplicationCall.token() = attributes[AttributeKey("token")] as TokensEntry
fun ApplicationCall.customer() = attributes[AttributeKey("customer")] as CustomersEntry
fun ApplicationCall.billing() = attributes[AttributeKey("billing")] as BillingsEntry
fun ApplicationCall.plan() = attributes[AttributeKey("plan")] as PlansEntry

suspend fun ApplicationCall.assertTokenPermissions(vararg permissions: TokenPermissions) =
    token().let { t -> permissions.all { t.hasPermission(it) } }.also {
        if (!it) respond(HttpStatusCode.Forbidden, "No permission")
    }
