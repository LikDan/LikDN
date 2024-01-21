package com.likdn.utils

import com.likdn.core.models.ApplicationMode
import com.likdn.core.models.MIMEType
import io.ktor.http.*

fun getApplicationMode(): ApplicationMode {
    val mode = System.getenv("mode")
    return ApplicationMode.values().find { it.name == mode } ?: ApplicationMode.DEBUG
}

val MIMEType.contentType: ContentType
    get() {
        val (contentType, contentSubType) = this.mimeType.first().split("/")
        return ContentType(contentType, contentSubType)
    }


fun MIMEType.Companion.getByContentType(contentType: ContentType): MIMEType = MIMEType.getByContentType(contentType.toString())