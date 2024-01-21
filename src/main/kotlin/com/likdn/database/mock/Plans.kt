package com.likdn.database.mock

import com.likdn.app.plans.models.PlansEntry
import org.jetbrains.exposed.sql.Transaction

fun Transaction.createPlans(): List<PlansEntry> {
    val starter = PlansEntry.new {
        this.name = "Starter"
        this.maxStorage = 4_294_967_296 //4gb
        this.maxFiles = 1500
        this.backup = false
        this.shortURL = true
    }

    val basic = PlansEntry.new {
        this.name = "Basic"
        this.maxStorage = 68_719_476_736 //64gb
        this.maxFiles = 20000
        this.backup = false
        this.shortURL = true
    }

    val premium = PlansEntry.new {
        this.name = "Premium"
        this.maxStorage = 1_099_511_627_776 //1tb
        this.maxFiles = null
        this.backup = false
        this.shortURL = true
    }

    val ultimate = PlansEntry.new {
        this.name = "Ultimate"
        this.maxStorage = 17_592_186_044_416 //16tb
        this.maxFiles = null
        this.backup = false
        this.shortURL = true
    }

    val flexible = PlansEntry.new {
        this.name = "Flexible"
        this.maxStorage = null
        this.maxFiles = null
        this.backup = false
        this.shortURL = true
    }

    return listOf(starter, basic, premium, ultimate, flexible)
}