package com.example.example

import com.google.gson.annotations.SerializedName


data class DraftOrderPost (

  @SerializedName("line_items"                   ) var lineItems                 : ArrayList<LineItemsPost> = arrayListOf(),
  @SerializedName("applied_discount"             ) var appliedDiscount           : AppliedDiscountPost?     = AppliedDiscountPost(),
  @SerializedName("customer"                     ) var customer                  : CustomerPost?            = CustomerPost(),
  @SerializedName("use_customer_default_address" ) var useCustomerDefaultAddress : Boolean?             = null

)