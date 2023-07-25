package com.example.example

import com.google.gson.annotations.SerializedName


data class AppliedDiscount (

  @SerializedName("description" ) var description : String? = null,
  @SerializedName("value"       ) var value       : String? = null,
  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("amount"      ) var amount      : String? = null,
  @SerializedName("value_type"  ) var valueType   : String? = null

)