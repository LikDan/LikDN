package com.likdn.app.billings.routes

import com.likdn.app.billings.controllers.BillingsController
import com.likdn.core.middlewares.auth.withAdminAuth
import com.likdn.app.billings.dto.CreateBillingDTO
import com.likdn.core.middlewares.exceptions.registerSwaggerStatuses
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.billing() = route("/billing") {
    withAdminAuth {
        post {
            registerSwaggerStatuses()

            val dto = call.receive<CreateBillingDTO>()
            val entity = BillingsController.createBilling(dto)
            call.respond(HttpStatusCode.Created, entity)
        }
    }

    get("payment") {
        call.respondFile(File("html/stripe.html"))
    }

    get {
        registerSwaggerStatuses()

        val planID = call.request.queryParameters["planID"]
        val entities = BillingsController.getBillings(planID)
        call.respond(HttpStatusCode.OK, entities)
    }

    get("/plans") {
        registerSwaggerStatuses()

        val entities = BillingsController.getBillingsWithPlan()
        call.respond(HttpStatusCode.OK, entities)
    }
}
