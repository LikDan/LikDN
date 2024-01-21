package com.likdn.app.payments.controllers

import com.likdn.app.payments.models.Payment
import com.likdn.app.payments.models.PaymentEvent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread

object BillingReceiver {
    private val json = Json { ignoreUnknownKeys = true }

    private val consumer by lazy {
        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = System.getenv("KAFKA_HOST")
        props[ConsumerConfig.GROUP_ID_CONFIG] = System.getenv("KAFKA_PAYMENTS_GROUP_ID")
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = "org.apache.kafka.common.serialization.StringDeserializer"
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] =
            "org.apache.kafka.common.serialization.StringDeserializer"
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props["security.protocol"] = "SASL_PLAINTEXT"
        props["sasl.mechanism"] = "PLAIN"
        props["sasl.jaas.config"] = System.getenv("KAFKA_SASL_CONFIG")

        val consumer = KafkaConsumer<String, String>(props)
        consumer.subscribe(listOf(System.getenv("KAFKA_PAYMENTS_TOPIC")))

        return@lazy consumer
    }

    fun startInThread() = thread { start() }

    fun start() {
        consumer.use { consumer ->
            while (true) {
                val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(1))
                records.forEach {
                    val payment = json.decodeFromString<Payment>(it.value())
                    when (payment.event) {
                        PaymentEvent.PAYMENT_PROCEEDED -> PaymentsController.proceedPayment(payment)
                        PaymentEvent.UNDEFINED -> {}
                    }
                }
            }
        }
    }
}
