package com.likdn.utils.migrations.schemes

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object MigrationsScheme : IdTable<String>("migrations") {
    override val id = varchar("id", 255).entityId().uniqueIndex()

    val migration = varchar("migration", 255)
    val number = integer("number")
    val status = enumerationByName<MigrationsStatus>("status", 10)
    val error = text("error").nullable()
    val startedAt = datetime("started_at").clientDefault { LocalDateTime.now() }
    val finishedAt = datetime("finished_at").nullable()
}