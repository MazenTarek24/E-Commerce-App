package com.example.example

import com.google.gson.annotations.SerializedName


data class Addresses (

  @SerializedName("address1"   ) var address1  : String? = null,
  @SerializedName("city"       ) var city      : String? = null,
  @SerializedName("province"   ) var province  : String? = null,
  @SerializedName("phone"      ) var phone     : String? = null,
  @SerializedName("zip"        ) var zip       : String? = null,
  @SerializedName("last_name"  ) var lastName  : String? = null,
  @SerializedName("first_name" ) var firstName : String? = null,
  @SerializedName("country"    ) var country   : String? = null

)