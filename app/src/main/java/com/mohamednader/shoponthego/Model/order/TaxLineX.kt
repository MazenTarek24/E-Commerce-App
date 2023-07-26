package com.mohamednader.shoponthego.Model.order

data class TaxLineX(
    val price: String,
    val price_set: PriceSetXXX,
    val rate: Double,
    val title: String
)