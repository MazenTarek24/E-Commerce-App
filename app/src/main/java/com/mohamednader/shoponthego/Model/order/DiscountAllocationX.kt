package com.mohamednader.shoponthego.Model.order

data class DiscountAllocationX(
    val amount: String,
    val amount_set: AmountSetX,
    val discount_application_index: Int
)