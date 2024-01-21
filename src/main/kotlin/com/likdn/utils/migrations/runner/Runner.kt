package com.likdn.utils.migrations.runner

import com.likdn.utils.migrations.schemes.*
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.Logger
import java.io.File
import java.time.LocalDateTime

object Runner {
    private val migrations = mutableListOf<Migration>()
    private var logger: Logger? = null


    fun setLogger(logger: Logger) {
        this.logger = logger
    }

    fun addMigration(name: String, number: Int = migrations.size + 1, id: String = name, m: MigrationFunc) =
        addMigration(Migration(id, number, name, m))

    fun addMigration(name: String, number: Int = migrations.size + 1, id: String = name, path: String) =
        addMigration(Migration(id, number, name) { loadFromFile(path) })

    fun addMigration(m: Migration) = migrations.add(m)

    fun runMigrations() {
        this.logger?.debug("retrieving migrations")
        val knownMigrations = retrieveMigrations()
        this.logger?.debug("filtering migrations")
        val migrationsToRun = this.migrations.filter {
            val km = knownMigrations.find { k -> it.id == k.id.value }
            if (km?.status === MigrationsStatus.FINISHED) return@filter false
            if (km != null) return@filter true

            transaction {
                MigrationEntry.new(it.id) {
                    this.migration = it.name
                    this.number = it.number
                    this.status = MigrationsStatus.PLANNED
                }
            }
            return@filter true
        }

        migrationsToRun.forEach(this::runMigration)
    }

    private fun retrieveMigrations(): List<MigrationEntry> = transaction {
        MigrationEntry.all().map { it }
    }

    private fun runMigration(m: Migration) = runCatching {
        this.logger?.info("running migration [${m.number}] ${m.id} - ${m.name}")
        transaction {
            MigrationsScheme.update({ MigrationsScheme.id eq m.id }) {
                it[status] = MigrationsStatus.STARTED
            }

            m.func(this)

            MigrationsScheme.update({ MigrationsScheme.id eq m.id }) {
                it[finishedAt] = LocalDateTime.now()
                it[status] = MigrationsStatus.FINISHED
            }
        }
    }.onFailure { ex ->
        this.logger?.error("error running migration [${m.number}] ${m.id} - ${m.name}", ex)
        transaction {
            MigrationsScheme.update({ MigrationsScheme.id eq m.id }) {
                it[status] = MigrationsStatus.FAILED
                it[error] = ex.message ?: "Unknown error occurred: $ex"
            }
        }
    }

    private fun Transaction.loadFromFile(path: String) {
        val query = File(path).reader().readText()
        exec(query)
    }
}