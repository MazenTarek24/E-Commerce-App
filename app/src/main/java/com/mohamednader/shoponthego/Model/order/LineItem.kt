package com.mohamednader.shoponthego.Model.order

data class LineItem(
    val id: Long,
    val name: String,
    val price: String,
    val product_id: Long,
    val title: String,
    val total_discount: String,
    val variant_id: Long,
    val variant_title: String,
    val vendor: Any
)