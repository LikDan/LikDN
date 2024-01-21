package com.likdn.app.billings.models

import com.likdn.app.billings.dto.BillingDTO
import com.likdn.app.plans.models.PlansEntry
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class BillingsEntry(id: EntityID<UUID>) : UUIDEntity(id) {
    var period by BillingsScheme.period
    var price by BillingsScheme.price
    var realPrice by BillingsScheme.realPrice
    var storageDiscount by BillingsScheme.storageDiscount
    var periodDiscount by BillingsScheme.periodDiscount
    var isAvailable by BillingsScheme.isAvailable
    var planID by BillingsScheme.planID
    val plan by PlansEntry referencedOn BillingsScheme.planID

    fun dto() = BillingDTO(
        id = this.id.value.toString(),
        period = this.period,
        price = this.price,
        planID = this.planID.toString()
    )

    companion object : UUIDEntityClass<BillingsEntry>(BillingsScheme)
}