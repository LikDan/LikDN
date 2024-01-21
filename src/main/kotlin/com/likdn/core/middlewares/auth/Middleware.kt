package com.likdn.core.middlewares.auth

import com.likdn.app.admin.controllers.AdminAuthController
import com.likdn.app.admin.models.AdminsEntry
import com.likdn.app.tokens.controllers.AuthController
import com.likdn.app.tokens.models.TokensEntry
import com.likdn.core.exceptions.ForbiddenException
import com.likdn.core.middlewares.exceptions.exceptionHandler
import com.likdn.core.models.ExceptionInformation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Route.withAuth(
    plugin: RouteScopedPlugin<Unit> = AuthPlugin,
    selection: RouteSelector = AuthorizationRouteSelector,
    build: Route.() -> Unit
): Route {
    val authenticatedRoute = createChild(selection)
    authenticatedRoute.exceptionHandler()
    authenticatedRoute.install(plugin)
    authenticatedRoute.build()
    return authenticatedRoute
}

fun Route.withAdminAuth(build: Route.() -> Unit): Route =
    withAuth(AdminAuthPlugin, AdminAuthorizationRouteSelector, build)

val AdminAuthPlugin = createRouteScopedPlugin(name = "AdminAuthPlugin") {
    onCall { it.adminAuth() }
}

val AuthPlugin = createRouteScopedPlugin(name = "AuthPlugin") {
    onCall {
        it.auth()
    }
}

fun <T : Any> ApplicationCall.attrOrNull(attr: String): T? {
    val key = AttributeKey<T>(attr)
    if (key !in attributes) {
        runCatching {
            this.auth()
        }.onFailure {
            return null
        }
    }

    return attributes.getOrNull(key)
}

fun ApplicationCall.adminOrNull(): AdminsEntry? = this.attrOrNull("admin")
fun ApplicationCall.admin(): AdminsEntry = this.adminOrNull() ?: throw ForbiddenException("No permission")

fun ApplicationCall.tokenOrNull(): TokensEntry? = this.attrOrNull("token")
fun ApplicationCall.token(): TokensEntry = this.tokenOrNull() ?: throw ForbiddenException("No permission")

private fun ApplicationCall.auth() = AuthController.auth(this)
private fun ApplicationCall.adminAuth() = AdminAuthController.auth(this)