package com.example.example

import com.google.gson.annotations.SerializedName


data class DraftOrderput (

  @SerializedName("id"               ) var id              : Long?                 = null,
  @SerializedName("applied_discount" ) var appliedDiscount : AppliedDiscountPut?     = AppliedDiscountPut(),
  @SerializedName("line_items"       ) var lineItems       : ArrayList<LineItemsput> = arrayListOf()
)