package com.likdn.plugins

import com.likdn.app.billings.BillingsScheme
import com.likdn.app.customers.CustomersScheme
import com.likdn.app.plans.PlansScheme
import com.likdn.app.storage.StorageScheme
import com.likdn.app.tokens.TokensScheme
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    Database.connect(
        url = System.getenv("DB_URL"),
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD"),
        driver = "org.postgresql.Driver"
    )

    transaction {
        SchemaUtils.create(PlansScheme, BillingsScheme, CustomersScheme, TokensScheme, StorageScheme)
    }
}
