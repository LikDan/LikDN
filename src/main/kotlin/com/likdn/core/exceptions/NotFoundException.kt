package com.likdn.core.exceptions

import io.ktor.http.*

class NotFoundException(message: String? = null) : HttpException(message, HttpStatusCode.NotFound)