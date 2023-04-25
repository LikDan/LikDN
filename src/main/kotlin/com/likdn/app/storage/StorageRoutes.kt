package com.likdn.app.storage

import com.likdn.app.auth.AuthPlugin
import com.likdn.app.auth.customer
import com.likdn.app.auth.plan
import com.likdn.app.auth.token
import com.likdn.app.auth.assertTokenPermissions
import com.likdn.app.tokens.TokenPermissions
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.storage() = route("storage") {
    install(AuthPlugin)
    post {
        val token = call.token()
        if (!token.hasPermission(TokenPermissions.WRITE)) {
            call.respond(HttpStatusCode.Forbidden, "No permission")
            return@post
        }

        val customer = call.customer()

        val params = mutableMapOf<String, String>()
        var file: PartData.FileItem? = null

        call.receiveMultipart().forEachPart {
            when (it) {
                is PartData.FormItem -> params[it.name ?: ""] = it.value
                is PartData.FileItem -> file = it
                else -> Unit
            }
        }

        file ?: return@post

        val plan = call.plan()

        val extension = file?.originalFileName?.substringAfterLast(".") ?: return@post
        val filename = "${generateFileName()}.$extension"

        customer.file(filename).outputStream().use { fos ->
            file!!.streamProvider().copyTo(fos)
        }

        val shorten = if (plan.shortURL) generateFileName(true) else null
        val origin = file!!.originalFileName ?: return@post
        val contentType = file!!.contentType ?: return@post

        val entry = transaction {
            StorageEntry.new {
                this.shortenFileName = shorten
                this.fileName = filename
                this.originFileName = origin
                this.contentType = contentType.contentType
                this.contentSubtype = contentType.contentSubtype
                this.data = params["data"]
                this.customerID = customer.id
                this.tokenID = token.id
            }
        }

        call.respond(HttpStatusCode.Created, entry.dto())
    }
    get("{filename}") {
        if (!call.assertTokenPermissions(TokenPermissions.READ)) return@get

        val filename = call.parameters["filename"] ?: ""
        val storage = transaction {
            StorageEntry.find { StorageScheme.fileName eq filename }.firstOrNull()
        } ?: return@get

        call.respondOutputStream(contentType = storage.contentType()) {
            call.customer().file(filename).inputStream().copyTo(this)
        }
    }
}

fun Route.shortStorage() = route("") {
    install(AuthPlugin)
    get("{filename}") { getFileByShortName(call) }
    get("cdn/{filename}") { getFileByShortName(call) }
}

suspend fun getFileByShortName(call: ApplicationCall) {
    if (!call.assertTokenPermissions(TokenPermissions.READ)) return

    val filename = call.parameters["filename"] ?: ""
    val storage = transaction {
        StorageEntry.find { StorageScheme.shortenFileName eq filename }.firstOrNull()
    } ?: return

    call.respondOutputStream(contentType = storage.contentType()) {
        call.customer().file(storage.fileName).inputStream().copyTo(this)
    }
}