package com.example.example

import com.google.gson.annotations.SerializedName


data class PresentmentPrices (

  @SerializedName("price"            ) var price          : Price?  = Price(),
  @SerializedName("compare_at_price" ) var compareAtPrice : String? = null

)