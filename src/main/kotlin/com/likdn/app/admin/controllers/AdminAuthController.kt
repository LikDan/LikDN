package com.likdn.app.admin.controllers

import com.likdn.app.admin.models.AdminsEntry
import com.likdn.app.admin.models.AdminsScheme
import com.likdn.core.exceptions.UnauthorizedException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction

object AdminAuthController {
    fun auth(call: ApplicationCall) {
        val auth = call.request.header("Authorization") ?: throw UnauthorizedException()
        val admin = transaction { AdminsEntry.find { AdminsScheme.token eq auth }.firstOrNull() }
            ?: throw UnauthorizedException()

        call.attributes.put(AttributeKey("admin"), admin)
    }
}