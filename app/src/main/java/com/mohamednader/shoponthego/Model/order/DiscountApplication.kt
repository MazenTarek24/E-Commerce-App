package com.mohamednader.shoponthego.Model.order

data class DiscountApplication(
    val allocation_method: String,
    val code: String,
    val target_selection: String,
    val target_type: String,
    val type: String,
    val value: String,
    val value_type: String
)