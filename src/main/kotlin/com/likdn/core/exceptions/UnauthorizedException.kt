package com.likdn.core.exceptions

import io.ktor.http.*

class UnauthorizedException(message: String? = null) : HttpException(message, HttpStatusCode.Unauthorized)