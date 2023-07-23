package com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency

data class ConvertCurrencyResponse(
    val terms : String,
    val privacy: String,
    val from: String,
    val amount: Double,
    val timestamp: String,
    val to: List<ToCurrency>
)
