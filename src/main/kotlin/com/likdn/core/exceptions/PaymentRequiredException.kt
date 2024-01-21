package com.likdn.core.exceptions

import io.ktor.http.*

class PaymentRequiredException(message: String? = null) : HttpException(message, HttpStatusCode.PaymentRequired)