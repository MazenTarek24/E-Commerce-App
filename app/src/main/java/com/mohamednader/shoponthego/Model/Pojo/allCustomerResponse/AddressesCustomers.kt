package com.example.example

import com.google.gson.annotations.SerializedName


data class AddressesCustomers (

  @SerializedName("id"            ) var id           : Int?     = null,
  @SerializedName("customer_id"   ) var customerId   : Int?     = null,
  @SerializedName("first_name"    ) var firstName    : String?  = null,
  @SerializedName("last_name"     ) var lastName     : String?  = null,
  @SerializedName("address1"      ) var address1     : String?  = null,
  @SerializedName("address2"      ) var address2     : String?  = null,
  @SerializedName("city"          ) var city         : String?  = null,
  @SerializedName("country"       ) var country      : String?  = null,
  @SerializedName("phone"         ) var phone        : String?  = null,
  @SerializedName("name"          ) var name         : String?  = null,
  @SerializedName("country_name"  ) var countryName  : String?  = null,

)