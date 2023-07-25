package com.example.example

import com.google.gson.annotations.SerializedName


data class ShippingLinemo (

  @SerializedName("title"  ) var title  : String?  = null,
  @SerializedName("custom" ) var custom : Boolean? = null,
  @SerializedName("handle" ) var handle : String?  = null,
  @SerializedName("price"  ) var price  : String?  = null

)