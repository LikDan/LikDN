package com.likdn.core.middlewares.exceptions

import com.likdn.core.exceptions.HttpException
import com.likdn.core.models.ApplicationMode
import com.likdn.core.models.ExceptionInformation
import com.likdn.utils.getApplicationMode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*

fun Route.exceptionHandler(mode: ApplicationMode = getApplicationMode()) {
    fun Throwable.message(default: String = this.toString()): String = this.localizedMessage ?: this.message ?: default
    fun Throwable.trace(): String? {
        if (mode == ApplicationMode.DEBUG) this.printStack()
        return if (mode == ApplicationMode.DEBUG) this.stackTraceToString() else null
    }

    suspend fun PipelineContext<*, ApplicationCall>.intercept() {
        try {
            this.proceed()
        } catch (ex: HttpException) {
            context.respond(
                ex.status,
                ExceptionInformation(
                    message = ex.message(ex.status.description),
                    status = ex.status.description,
                    trace = ex.trace()
                )
            )
        } catch (ex: Throwable) {
            context.respond(
                HttpStatusCode.InternalServerError,
                ExceptionInformation(
                    message = ex.message(),
                    status = HttpStatusCode.InternalServerError.description,
                    trace = ex.trace()
                )
            )
        }
    }

    intercept(ApplicationCallPipeline.Call) { intercept() }
    intercept(ApplicationCallPipeline.Setup) { intercept() }
    intercept(ApplicationCallPipeline.Plugins) { intercept() }
    intercept(ApplicationCallPipeline.Fallback) { intercept() }
    intercept(ApplicationCallPipeline.Monitoring) { intercept() }
}
