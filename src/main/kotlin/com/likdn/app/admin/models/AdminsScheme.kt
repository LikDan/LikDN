package com.likdn.app.admin.models

import com.likdn.utils.UUIDTimedTable
import com.likdn.utils.sql.array.array

object AdminsScheme : UUIDTimedTable("admins") {
    val name = varchar("name", 255)
    val token = varchar("token", 64).uniqueIndex()
    val permissions = enumerationByName<AdminPermissions>("permissions", 25).array()
}