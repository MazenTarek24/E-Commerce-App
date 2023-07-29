package com.example.example

import com.google.gson.annotations.SerializedName


data class Customers (

  @SerializedName("id"                           ) var id                        : Int?                 = null,
  @SerializedName("email"                        ) var email                     : String?              = null,
  @SerializedName("created_at"                   ) var createdAt                 : String?              = null,
  @SerializedName("updated_at"                   ) var updatedAt                 : String?              = null,
  @SerializedName("first_name"                   ) var firstName                 : String?              = null,
  @SerializedName("last_name"                    ) var lastName                  : String?              = null,
  @SerializedName("orders_count"                 ) var ordersCount               : Int?                 = null,
  @SerializedName("state"                        ) var state                     : String?              = null,
  @SerializedName("total_spent"                  ) var totalSpent                : String?              = null,
  @SerializedName("last_order_id"                ) var lastOrderId               : String?              = null,
  @SerializedName("verified_email"               ) var verifiedEmail             : Boolean?             = null,
  @SerializedName("last_order_name"              ) var lastOrderName             : String?              = null,
  @SerializedName("currency"                     ) var currency                  : String?              = null,
  @SerializedName("phone"                        ) var phone                     : String?              = null,
  @SerializedName("addresses"                    ) var addresses                 : ArrayList<AddressesCustomers> = arrayListOf(),

)