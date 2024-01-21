package com.likdn.app.plans.controllers

import com.likdn.app.admin.models.AdminPermissions
import com.likdn.app.admin.models.AdminsEntry
import com.likdn.app.plans.dto.CreatePlanDTO
import com.likdn.app.plans.dto.PlanDTO
import com.likdn.app.plans.models.PlansEntry
import com.likdn.core.permissions.assertPermission
import org.jetbrains.exposed.sql.transactions.transaction


object PlansController {
    fun createPlan(admin: AdminsEntry, dto: CreatePlanDTO): PlanDTO {
        admin.assertPermission(AdminPermissions.MANAGE_PLANS)

        val entry = transaction {
            PlansEntry.new {
                this.name = dto.name
                this.maxStorage = dto.maxStorage
                this.maxFiles = dto.maxFiles
                this.backup = dto.backup
                this.shortURL = dto.shortURL
            }
        }
        return entry.dto()
    }

    fun getPlans(): List<PlanDTO> {
        return transaction { PlansEntry.all().map { it.dto() } }
    }
}
