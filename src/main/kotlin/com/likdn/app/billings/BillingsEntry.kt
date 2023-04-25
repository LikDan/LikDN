package com.likdn.app.billings

import com.likdn.app.plans.PlansEntry
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class BillingsEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var period by BillingsScheme.period
    var price by BillingsScheme.price
    var planID by BillingsScheme.planID
    val plan by PlansEntry referencedOn BillingsScheme.planID

    companion object : UUIDEntityClass<BillingsEntry>(BillingsScheme)
}