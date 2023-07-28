package com.mohamednader.shoponthego.Model.order

data class Customer(
    val created_at: String,
    val currency: String,
    val email: String,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val last_order_id: Int,
    val phone: String,
    val updated_at: String,
    val verified_email: Boolean
)