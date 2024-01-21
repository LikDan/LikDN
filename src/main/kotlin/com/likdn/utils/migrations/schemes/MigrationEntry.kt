package com.likdn.utils.migrations.schemes

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MigrationEntry(id: EntityID<String>) : Entity<String>(id) {
    var migration by MigrationsScheme.migration
    var number by MigrationsScheme.number
    var status by MigrationsScheme.status
    var startedAt by MigrationsScheme.startedAt
    var finishedAt by MigrationsScheme.finishedAt

    companion object : EntityClass<String, MigrationEntry>(MigrationsScheme)
}