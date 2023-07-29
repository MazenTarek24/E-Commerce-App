package com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies

import com.google.gson.annotations.SerializedName

data class CurrencyInfo(
        var iso: String,
        @SerializedName("currency_name") var currencyName: String,
)
