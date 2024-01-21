package com.likdn.plugins

import com.likdn.app.billings.routes.billing
import com.likdn.app.customers.routes.customers
import com.likdn.app.plans.routes.plans
import com.likdn.app.storage.routes.shortStorage
import com.likdn.app.storage.routes.storage
import com.likdn.app.tokens.routes.tokens
import com.likdn.core.middlewares.exceptions.exceptionHandler
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Application.configureRouting() = routing {
    exceptionHandler()
    swagger()

    route("api") {
        storage()
        plans()
        billing()
        customers()
        tokens()
    }
    shortStorage()
}

fun Routing.swagger() {
    get("redocly") {
        call.respondFile(File("html/redocly.html"))
    }

    get("yaml-swagger") {
        val file = File(this.javaClass.getResource("/openapi/documentation.yaml")?.path ?: "")
        call.respondFile(file)
    }

    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
}
