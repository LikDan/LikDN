package com.likdn.utils.migrations.schemes

import org.jetbrains.exposed.sql.Transaction

typealias MigrationFunc = Transaction.() -> Any

data class Migration(
    val id: String,
    val number: Int,
    val name: String,
    val func: MigrationFunc,
)