package com.likdn.plugins

import com.likdn.app.admin.models.AdminsScheme
import com.likdn.app.billings.models.BillingsScheme
import com.likdn.app.customers.models.CustomersScheme
import com.likdn.app.plans.models.PlansScheme
import com.likdn.app.storage.models.StorageScheme
import com.likdn.app.tokens.models.TokensScheme
import com.likdn.database.mock.createBillings
import com.likdn.database.mock.createPlans
import com.likdn.utils.migrations.runner.Runner
import com.likdn.utils.migrations.schemes.MigrationsScheme
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    Database.connect(
        url = System.getenv("POSTGRES_URL"),
        user = System.getenv("POSTGRES_USER"),
        password = System.getenv("POSTGRES_PASSWORD"),
        driver = "org.postgresql.Driver"
    )

    transaction {
        SchemaUtils.create(
            PlansScheme,
            BillingsScheme,
            CustomersScheme,
            TokensScheme,
            StorageScheme,
            AdminsScheme,
            MigrationsScheme,
        )
    }

    Runner.setLogger(this.log)
    Runner.addMigration("create plans") { createPlans() }
    Runner.addMigration("create billings") { createBillings() }
    Runner.addMigration("create plans_billings view", path = "migrations/create_plans_billings_view.sql")
    Runner.runMigrations()
}
