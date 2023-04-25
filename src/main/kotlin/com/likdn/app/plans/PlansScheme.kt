package com.likdn.app.plans

import org.jetbrains.exposed.dao.id.UUIDTable

object PlansScheme : UUIDTable("plans") {
    val name = varchar("name", 25)
    val maxStorage = long("max_storage").nullable()
    val maxFiles = long("max_filed").nullable()
    val backup = bool("backup")
    val shortURL = bool("short_url")
}