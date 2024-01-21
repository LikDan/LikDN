package com.likdn.app.tokens.controllers

import com.likdn.core.exceptions.ForbiddenException
import com.likdn.core.exceptions.NotFoundException
import com.likdn.app.tokens.models.TokenPermissions
import com.likdn.app.tokens.dto.CreateTokenDTO
import com.likdn.app.tokens.dto.TokenDTO
import com.likdn.app.tokens.models.TokensEntry
import com.likdn.app.tokens.models.TokensScheme
import com.likdn.core.permissions.assertPermission
import com.likdn.utils.randomString
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*


object TokensController {
    fun getAllTokens(token: TokensEntry): List<TokenDTO> {
        token.assertPermission(TokenPermissions.MANAGE_TOKENS)

        return transaction {
            TokensEntry.find { TokensScheme.customerID eq token.customerID }.map { it.credentialDTO() }
        }
    }

    fun createToken(dto: CreateTokenDTO, token: TokensEntry): TokenDTO {
        token.assertPermission(TokenPermissions.MANAGE_TOKENS)

        val canHavePermissions = dto.permissions
            .any {
                it != TokenPermissions.ALL &&
                        it != TokenPermissions.MANAGE_TOKENS &&
                        it != TokenPermissions.MANAGE_BILLING
            }
        if (!canHavePermissions) {
            throw ForbiddenException("You can have only one token with 'MANAGE_TOKENS' or 'MANAGE_BILLING' permissions")
        }

        val newToken = transaction {
            TokensEntry.new {
                this.token = randomString()
                this.name = dto.name
                this.expiredAt = LocalDateTime.parse(dto.expireAt)
                this.issuedAt = LocalDateTime.now()
                this.permissions = dto.permissions
                this.mimeTypes = dto.mimeTypes
                this.groups = dto.groups
                this.customerID = token.customerID
            }
        }

        return newToken.dto()
    }

    fun deleteToken(token: TokensEntry, tokenID: String) {
        token.assertPermission(TokenPermissions.MANAGE_TOKENS)

        val tokenUUID = UUID.fromString(tokenID)

        val amount = transaction {
            TokensScheme.deleteWhere { (id eq tokenUUID) and (customerID eq token.customerID) and not(isRoot) }
        }

        if (amount == 0) throw NotFoundException()
    }
}
