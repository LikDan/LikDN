package com.likdn.database.mock

import com.likdn.app.billings.models.BillingPeriod
import com.likdn.app.billings.models.BillingsEntry
import com.likdn.app.plans.models.PlansEntry
import org.jetbrains.exposed.sql.Transaction
import kotlin.math.pow
import kotlin.math.roundToInt

fun Transaction.createBillings() {
    val plans = PlansEntry.all().map { it }

    val pricePerBytePerMonth = 0.00000000005f
    val pricePerBytePerWeek = pricePerBytePerMonth / 4.33f
    val pricePerBytePerYear = pricePerBytePerMonth * 12

    val realPricePerBytePerMonth = 0.0000000000214204189453125
    val realPricePerBytePerWeek = realPricePerBytePerMonth / 4.33f
    val realPricePerBytePerYear = realPricePerBytePerMonth * 12

    val periodWeekDiscount = -0.2f
    val periodMonthDiscount = 0.0f
    val periodYearDiscount = 0.1f

    plans.forEach {
        if (it.maxStorage == null)
            it.maxStorage ?: run {
                BillingsEntry.new {
                    this.period = BillingPeriod.DAILY
                    this.price = -1.0f
                    this.realPrice = -1.0f
                    this.storageDiscount = 0f
                    this.periodDiscount = 0f
                    this.isAvailable = false
                    this.planID = it.id
                }
                return@forEach
            }

        val storageDiscount = it.maxStorage!!.toDouble().pow(1.0 / 3.0).div(1 * 10.0.pow(5)).toFloat()

        BillingsEntry.new {
            this.period = BillingPeriod.WEEKLY
            this.price =
                (it.maxStorage!! * (1 - storageDiscount - periodWeekDiscount) * pricePerBytePerWeek).roundToPrice()
            this.realPrice = (it.maxStorage!! * realPricePerBytePerWeek).toFloat()
            this.storageDiscount = storageDiscount
            this.periodDiscount = periodWeekDiscount
            this.isAvailable = true
            this.planID = it.id
        }

        BillingsEntry.new {
            this.period = BillingPeriod.MONTHLY
            this.price =
                (it.maxStorage!! * (1 - storageDiscount - periodMonthDiscount) * pricePerBytePerMonth).roundToPrice()
            this.realPrice = (it.maxStorage!! * realPricePerBytePerMonth).toFloat()
            this.storageDiscount = storageDiscount
            this.periodDiscount = periodMonthDiscount
            this.isAvailable = true
            this.planID = it.id
        }

        BillingsEntry.new {
            this.period = BillingPeriod.ANNUALLY
            this.price =
                (it.maxStorage!! * (1 - storageDiscount - periodYearDiscount) * pricePerBytePerYear).roundToPrice()
            this.realPrice = (it.maxStorage!! * realPricePerBytePerYear).toFloat()
            this.storageDiscount = storageDiscount
            this.periodDiscount = periodYearDiscount
            this.isAvailable = true
            this.planID = it.id
        }
    }
}

private fun Float.roundToPrice(): Float = (this * 100).roundToInt() / 100.0f
