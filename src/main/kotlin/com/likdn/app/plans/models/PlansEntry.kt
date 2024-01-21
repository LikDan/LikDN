package com.likdn.app.plans.models

import com.likdn.app.plans.dto.PlanDTO
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PlansEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by PlansScheme.name
    var maxStorage by PlansScheme.maxStorage
    var maxFiles by PlansScheme.maxFiles
    var backup by PlansScheme.backup
    var shortURL by PlansScheme.shortURL

    fun dto(): PlanDTO =
        PlanDTO(
            id = this.id.value.toString(),
            name = this.name,
            maxStorage = this.maxStorage,
            maxFiles = this.maxFiles,
            backup = this.backup,
            shortURL = this.shortURL,
        )

    companion object : UUIDEntityClass<PlansEntry>(PlansScheme)
}