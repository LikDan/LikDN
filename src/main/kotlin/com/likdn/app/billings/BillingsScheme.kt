package com.likdn.app.billings

import com.likdn.app.plans.PlansScheme
import org.jetbrains.exposed.dao.id.UUIDTable

object BillingsScheme : UUIDTable("billings") {
    val period = enumeration<BillingPeriod>("period")
    val price = float("price")
    val planID = reference("plan_id", PlansScheme.id)
}