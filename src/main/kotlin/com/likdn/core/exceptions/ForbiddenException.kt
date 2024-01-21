package com.likdn.core.exceptions

import io.ktor.http.*

class ForbiddenException(message: String) : HttpException(message, HttpStatusCode.Forbidden)