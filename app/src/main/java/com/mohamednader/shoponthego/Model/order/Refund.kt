package com.mohamednader.shoponthego.Model.order

data class Refund(
    val admin_graphql_api_id: String,
    val created_at: String,
    val duties: List<Any>,
    val id: Long,
    val note: String,
    val order_adjustments: List<Any>,
    val order_id: Long,
    val processed_at: String,
    val refund_line_items: List<RefundLineItem>,
    val restock: Boolean,
    val total_duties_set: TotalDutiesSet,
    val transactions: List<Transaction>,
    val user_id: Long
)