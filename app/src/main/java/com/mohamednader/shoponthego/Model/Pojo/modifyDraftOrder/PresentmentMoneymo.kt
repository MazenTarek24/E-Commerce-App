package com.example.example

import com.google.gson.annotations.SerializedName


data class PresentmentMoneymo (

  @SerializedName("amount"        ) var amount       : String? = null,
  @SerializedName("currency_code" ) var currencyCode : String? = null

)