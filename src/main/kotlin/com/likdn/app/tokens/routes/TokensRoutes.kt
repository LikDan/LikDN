package com.likdn.app.tokens.routes

import com.likdn.app.tokens.controllers.TokensController
import com.likdn.core.exceptions.UnappropriatedContentException
import com.likdn.core.middlewares.auth.AuthPlugin
import com.likdn.core.middlewares.auth.token
import com.likdn.core.middlewares.auth.withAuth
import com.likdn.app.tokens.models.TokenPermissions
import com.likdn.app.tokens.dto.CreateTokenDTO
import com.likdn.core.middlewares.exceptions.registerSwaggerStatuses
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tokens() = route("tokens") {
    withAuth(AuthPlugin) {
        post {
            registerSwaggerStatuses()

            val token = call.token()
            val dto = call.receive<CreateTokenDTO>()
            val entry = TokensController.createToken(dto, token)
            call.respond(HttpStatusCode.Created, entry)
        }

        delete("{id}") {
            registerSwaggerStatuses()

            val id = call.parameters["id"] ?: throw UnappropriatedContentException("id")
            val token = call.token()
            TokensController.deleteToken(token, id)
            call.respond(HttpStatusCode.NoContent)
        }

        get {
            registerSwaggerStatuses()

            val token = call.token()
            val entries = TokensController.getAllTokens(token)
            call.respond(HttpStatusCode.OK, entries)
        }
    }
}