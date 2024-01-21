package com.likdn.core.middlewares.exceptions

import com.likdn.core.models.ExceptionInformation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*


suspend fun PipelineContext<*, ApplicationCall>.registerSwaggerStatuses() {
    //used for automatic OpenAPI decs
    if (false) {
        val info = ExceptionInformation("message text", "status text")

        call.respond(HttpStatusCode.Forbidden, info)
        call.respond(HttpStatusCode.PaymentRequired, info)
        call.respond(HttpStatusCode.Unauthorized, info)
        call.respond(HttpStatusCode.UnprocessableEntity, info)
        call.respond(HttpStatusCode.NotFound, info)
        call.respond(HttpStatusCode.InternalServerError, info)
    }
}