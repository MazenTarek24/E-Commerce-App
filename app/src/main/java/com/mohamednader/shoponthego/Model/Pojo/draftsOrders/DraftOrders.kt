package com.example.example

import com.google.gson.annotations.SerializedName


data class DraftOrders (

  @SerializedName("id"                   ) var id                : Long               = 10,
  @SerializedName("email"                ) var email             : String?              = null,
  @SerializedName("taxes_included"       ) var taxesIncluded     : Boolean?             = null,
  @SerializedName("currency"             ) var currency          : String?              = null,
  @SerializedName("name"                 ) var name              : String?              = null,
  @SerializedName("status"               ) var status            : String?              = null,
  @SerializedName("order_id"             ) var orderId           : Long?                 = null,
  @SerializedName("total_price"          ) var totalPrice        : String?              = null,
  @SerializedName("subtotal_price"       ) var subtotalPrice     : String?              = null,
  @SerializedName("customer"             ) var customer          : CustomerDraftsOrders?= CustomerDraftsOrders()

)