package com.example.example

import com.google.gson.annotations.SerializedName


data class Price (

  @SerializedName("amount"        ) var amount       : String? = null,
  @SerializedName("currency_code" ) var currencyCode : String? = null

)