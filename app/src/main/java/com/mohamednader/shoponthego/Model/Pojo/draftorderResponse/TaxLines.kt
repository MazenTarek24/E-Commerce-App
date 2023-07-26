package com.example.example

import com.google.gson.annotations.SerializedName


data class TaxLines (

  @SerializedName("rate"  ) var rate  : Double? = null,
  @SerializedName("title" ) var title : String? = null,
  @SerializedName("price" ) var price : String? = null

)