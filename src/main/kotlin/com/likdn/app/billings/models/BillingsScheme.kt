package com.likdn.app.billings.models

import com.likdn.app.plans.models.PlansScheme
import com.likdn.utils.UUIDTimedTable

object BillingsScheme : UUIDTimedTable("billings") {
    val period = enumerationByName<BillingPeriod>("period", 25)
    val price = float("price")
    val realPrice = float("real_price")
    val storageDiscount = float("storage_discount")
    val periodDiscount = float("period_discount")
    val isAvailable = bool("available")
    val planID = reference("plan_id", PlansScheme.id)
}