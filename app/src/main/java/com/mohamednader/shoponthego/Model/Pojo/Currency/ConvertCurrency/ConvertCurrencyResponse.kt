package com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency

data class ConvertCurrencyResponse(
        var terms: String,
        var privacy: String,
        var from: String,
        var amount: Double,
        var timestamp: String,
        var to: List<ToCurrency>
)
