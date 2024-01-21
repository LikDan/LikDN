package com.likdn.app.tokens.controllers

import com.likdn.app.tokens.models.TokensEntry
import com.likdn.app.tokens.models.TokensScheme
import com.likdn.core.exceptions.UnauthorizedException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction

object AuthController {
    fun auth(call: ApplicationCall) {
        val auth = call.request.header("Authorization") ?: throw UnauthorizedException()
        val token = transaction { TokensEntry.find { TokensScheme.token eq auth }.firstOrNull() }
            ?: throw UnauthorizedException()

        call.attributes.put(AttributeKey("token"), token)
    }
}