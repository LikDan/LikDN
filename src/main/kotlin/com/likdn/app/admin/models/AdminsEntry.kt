package com.likdn.app.admin.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class AdminsEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by AdminsScheme.name
    val token by AdminsScheme.token
    val permissions by AdminsScheme.permissions

    fun hasPermission(permission: AdminPermissions) = permission in permissions

    companion object : UUIDEntityClass<AdminsEntry>(AdminsScheme)
}