package com.example.example

import com.google.gson.annotations.SerializedName


data class LineItemsPost (

  @SerializedName("title"    ) var title    : String? = null,
  @SerializedName("price"    ) var price    : String? = null,
  @SerializedName("quantity" ) var quantity : Int?    = null

)