package com.mohamednader.shoponthego.Model.order

data class DiscountAllocation(
    val amount: String,
    val amount_set: AmountSet,
    val discount_application_index: Int
)