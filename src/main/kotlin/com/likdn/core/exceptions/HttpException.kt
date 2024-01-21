package com.likdn.core.exceptions

import io.ktor.http.*

open class HttpException(message: String?, val status: HttpStatusCode) : Exception(message)
