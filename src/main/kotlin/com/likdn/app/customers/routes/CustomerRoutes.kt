package com.likdn.app.customers.routes

import com.likdn.app.customers.controllers.CustomersController
import com.likdn.app.customers.dto.CreateCustomerDTO
import com.likdn.app.customers.dto.CreateCustomerResponseDTO
import com.likdn.core.middlewares.auth.AuthPlugin
import com.likdn.core.middlewares.auth.token
import com.likdn.core.middlewares.auth.withAuth
import com.likdn.core.middlewares.exceptions.registerSwaggerStatuses
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customers() = route("/customers") {
    withAuth(AuthPlugin) {
        get("self") {
            registerSwaggerStatuses()

            val token = call.token()
            val entry = CustomersController.getCustomerByToken(token)
            call.respond(HttpStatusCode.OK, entry)
        }

        //todo change name

        //todo -> active tokens, current stats, balance...
        get("dashboard") {

        }

        //todo, for now use stripe dashboard
//        patch("billing") {
//            call.token(TokenPermissions.MANAGE_BILLING)
//            val billingID = call.receive<String>()
//            val customer = call.customer()
//            CustomersController.changeBillingPlan(customer, billingID)
//        }

        delete {
            registerSwaggerStatuses()

            val token = call.token()
            CustomersController.deleteCustomer(token)
        }
    }

    post {
        registerSwaggerStatuses()

        val dto = call.receive<CreateCustomerDTO>()
        val entry = CustomersController.createCustomer(dto, call.request.origin.remoteAddress)

        val response = CreateCustomerResponseDTO(
            customer = entry,
            paymentDescription = "to continue to payment please follow this link",
            paymentLink = "http://localhost:8080/api/billing/payment"
        )

        call.respond(HttpStatusCode.Created, response)
    }
}
