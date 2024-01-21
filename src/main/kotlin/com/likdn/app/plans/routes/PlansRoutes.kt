package com.likdn.app.plans.routes

import com.likdn.app.plans.controllers.PlansController
import com.likdn.core.middlewares.auth.AdminAuthPlugin
import com.likdn.core.middlewares.auth.admin
import com.likdn.core.middlewares.auth.withAuth
import com.likdn.app.admin.models.AdminPermissions
import com.likdn.app.plans.dto.CreatePlanDTO
import com.likdn.core.middlewares.exceptions.registerSwaggerStatuses
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.plans() = route("/plans") {
    withAuth(AdminAuthPlugin) {
        post {
            registerSwaggerStatuses()

            val admin = call.admin()
            val dto = call.receive<CreatePlanDTO>()
            val entry = PlansController.createPlan(admin, dto)
            call.respond(HttpStatusCode.Created, entry)
        }
    }

    get {
        registerSwaggerStatuses()

        val plans = PlansController.getPlans()
        call.respond(plans)
    }
}
