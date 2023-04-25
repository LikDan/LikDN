package com.likdn.plugins

import com.likdn.app.storage.shortStorage
import com.likdn.app.storage.storage
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        shortStorage()
        route("api") {
            storage()
            //todo auth/plans/
            //todo info (/plan, /status, /billingInfo, /upgradePlan...)
        }
    }
}
