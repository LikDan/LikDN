package com.likdn.core.permissions

import com.likdn.app.admin.models.AdminPermissions
import com.likdn.app.admin.models.AdminsEntry
import com.likdn.app.tokens.models.TokenPermissions
import com.likdn.app.tokens.models.TokensEntry
import com.likdn.core.exceptions.ForbiddenException
import com.likdn.core.middlewares.auth.admin
import io.ktor.server.application.*

fun TokensEntry.assertPermission(vararg permissions: TokenPermissions) {
    val has = this.hasPermission(TokenPermissions.ALL) || permissions.all { this.hasPermission(it) }
    if (!has) {
        throw ForbiddenException("No permission")
    }
}

fun AdminsEntry.assertPermission(vararg permissions: AdminPermissions) {
    val has = permissions.all { this.hasPermission(it) }
    if (!has) {
        throw ForbiddenException("No permission")
    }
}
