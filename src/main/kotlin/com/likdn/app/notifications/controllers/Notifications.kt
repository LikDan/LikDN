package com.likdn.app.notifications.controllers

import com.likdn.app.notifications.models.Notification
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

object Notifications {
    private val json = Json { ignoreUnknownKeys = true }

    private val producer by lazy {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = System.getenv("KAFKA_HOST")
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = "org.apache.kafka.common.serialization.StringSerializer"
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] =
            "org.apache.kafka.common.serialization.StringSerializer"
        props["security.protocol"] = "SASL_PLAINTEXT"
        props["sasl.mechanism"] = "PLAIN"
        props["sasl.jaas.config"] = System.getenv("KAFKA_SASL_CONFIG")

        val producer = KafkaProducer<String, String>(props)

        return@lazy producer
    }

    private val topic = System.getenv("KAFKA_NOTIFICATIONS_TOPIC")

    fun send(notification: Notification) {
        val message = json.encodeToString(notification)
        val record = ProducerRecord(topic, notification.type.toString(), message)
        producer.send(record)
    }
}