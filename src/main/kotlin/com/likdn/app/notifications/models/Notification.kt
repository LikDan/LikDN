package com.likdn.app.notifications.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val type: NotificationType,
    val contact: String,
    val vars: Map<String, String>? = null,
    val text: String? = null,
    val templateID: String? = null,
)