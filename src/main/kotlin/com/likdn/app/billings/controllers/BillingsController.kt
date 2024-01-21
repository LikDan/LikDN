package com.likdn.app.billings.controllers

import com.likdn.app.billings.models.BillingPeriod
import com.likdn.app.billings.dto.BillingDTO
import com.likdn.app.billings.dto.BillingWithPlanDTO
import com.likdn.app.billings.dto.CreateBillingDTO
import com.likdn.app.billings.models.BillingsEntry
import com.likdn.app.billings.models.BillingsScheme
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


object BillingsController {
    fun createBilling(dto: CreateBillingDTO): BillingDTO {
        val entity = transaction {
            BillingsEntry.new {
                this.period = dto.period
                this.price = dto.price
                this.isAvailable = dto.isAvailable
                this.planID = EntityID(UUID.fromString(dto.planID), BillingsScheme)
            }
        }

        return entity.dto()
    }

    fun getBillings(planID: String? = null): List<BillingDTO> {
        val entities = transaction {
            val res = if (planID == null) BillingsEntry.all()
            else BillingsEntry.find { BillingsScheme.planID eq EntityID(UUID.fromString(planID), BillingsScheme) }

            res.map { it.dto() }
        }

        return entities
    }

    fun getBillingsWithPlan(): List<BillingWithPlanDTO> {
        val dto = transaction {
            exec("select * from public.plans_billings WHERE available") {
                val entries = mutableListOf<BillingWithPlanDTO>()
                while (it.next()) {
                    val dto = BillingWithPlanDTO(
                        billingID = it.getObject("billing_id", UUID::class.java).toString(),
                        planID = it.getObject("plan_id", UUID::class.java).toString(),
                        name = it.getString("name"),
                        period = BillingPeriod.valueOf(it.getString("period")),
                        maxFiles = it.getLong("max_files"),
                        maxStorage = it.getString("max_storage"),
                        price = it.getString("price"),
                        shortURL = it.getBoolean("short_url"),
                    )

                    entries.add(dto)
                }
                return@exec entries.toList()
            } ?: emptyList()
        }

        return dto
    }
}
