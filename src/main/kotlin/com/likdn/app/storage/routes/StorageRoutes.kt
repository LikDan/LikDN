package com.likdn.app.storage.routes

import com.likdn.app.storage.controllers.StorageController
import com.likdn.app.storage.dto.CreateStorageDTO
import com.likdn.core.middlewares.auth.AuthPlugin
import com.likdn.core.middlewares.auth.token
import com.likdn.core.middlewares.auth.tokenOrNull
import com.likdn.core.middlewares.auth.withAuth
import com.likdn.core.middlewares.exceptions.registerSwaggerStatuses
import com.likdn.utils.contentType
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlin.collections.getOrDefault
import kotlin.collections.mutableMapOf
import kotlin.collections.set

fun Route.storage() = route("storage") {
    withAuth(AuthPlugin) {
        post {
            registerSwaggerStatuses()

            val token = call.token()

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

            val dto = CreateStorageDTO(
                file = file!!,
                isPublic = params.getOrDefault("public", "") == "true",
                group = params.getOrDefault("group", null),
                data = params.getOrDefault("data", null),
            )

            val entry = StorageController.create(token, dto)
            call.respond(HttpStatusCode.Created, entry)
        }

        delete("{filename}") {
            registerSwaggerStatuses()

            val filename = call.parameters["filename"] ?: ""
            val token = call.token()

            val entry = StorageController.delete(token, filename)
            call.respond(HttpStatusCode.OK, entry)
        }

        get {
            registerSwaggerStatuses()

            val token = call.token()
            val entries = StorageController.getCustomerEntries(token)
            call.respond(entries)
        }
    }

    get("{filename}") {
        registerSwaggerStatuses()

        val filename = call.parameters["filename"] ?: ""
        val token = call.tokenOrNull()

        val (entry, stream) = StorageController.getStreamByFullFileName(token, filename)

        call.respondOutputStream(contentType = entry.mimeType.contentType) {
            stream.copyTo(this)
        }
    }

    get("{filename}/info") {
        registerSwaggerStatuses()

        val filename = call.parameters["filename"] ?: ""
        val token = call.tokenOrNull()

        val entry = StorageController.getEntryByFullFileName(token, filename)
        call.respond(entry)
    }
}

fun Route.shortStorage() = route("") {
    get("{filename}") { getFileByShortName() }
    get("cdn/{filename}") { getFileByShortName() }

    get("{filename}/info") { getFileInfoByShortName() }
    get("cdn/{filename}/info") { getFileInfoByShortName() }
}

private suspend fun PipelineContext<*, ApplicationCall>.getFileByShortName() {
    registerSwaggerStatuses()

    val filename = call.parameters["filename"] ?: ""
    val token = call.tokenOrNull()

    val (entry, stream) = StorageController.getStreamByShortFileName(token, filename)

    call.respondOutputStream(contentType = entry.mimeType.contentType) {
        stream.copyTo(this)
    }
}

private suspend fun PipelineContext<*, ApplicationCall>.getFileInfoByShortName() {
    registerSwaggerStatuses()

    val filename = call.parameters["filename"] ?: ""
    val token = call.tokenOrNull()

    val entry = StorageController.getInfoByShortenFileName(token, filename)
    call.respond(entry)
}