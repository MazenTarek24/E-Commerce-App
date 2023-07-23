package com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies

import com.google.gson.annotations.SerializedName

data class CurrencyInfo(
    val iso: String,
    @SerializedName("currency_name") val currencyName: String,
)
