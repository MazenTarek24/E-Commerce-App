package com.mohamednader.shoponthego.Model.order

data class TaxLine(
    val price: String,
    val price_set: PriceSetX,
    val rate: Double,
    val title: String
)