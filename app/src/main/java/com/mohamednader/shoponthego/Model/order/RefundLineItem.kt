package com.mohamednader.shoponthego.Model.order

data class RefundLineItem(
    val id: Long,
    val line_item: LineItemXX,
    val line_item_id: Long,
    val location_id: Long,
    val quantity: Int,
    val restock_type: String,
    val subtotal: Double,
    val subtotal_set: SubtotalSet,
    val total_tax: Double,
    val total_tax_set: TotalTaxSet
)