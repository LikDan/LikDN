package com.likdn.plugins

import com.likdn.app.payments.controllers.BillingReceiver
import io.ktor.server.application.*

fun Application.configureKafka() {
    BillingReceiver.startInThread()
}
