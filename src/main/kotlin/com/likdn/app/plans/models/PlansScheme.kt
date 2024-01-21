package com.likdn.app.plans.models

import com.likdn.utils.UUIDTimedTable
import org.jetbrains.exposed.dao.id.UUIDTable

object PlansScheme : UUIDTimedTable("plans") {
    val name = varchar("name", 25)
    val maxStorage = long("max_storage").nullable()
    val maxFiles = long("max_files").nullable()
    val backup = bool("backup")
    val shortURL = bool("short_url")
}