package com.example.example

import com.google.gson.annotations.SerializedName


data class AddressesCustomers (

  @SerializedName("id"            ) var id           : Int?     = null,
  @SerializedName("customer_id"   ) var customerId   : Int?     = null,
  @SerializedName("first_name"    ) var firstName    : String?  = null,
  @SerializedName("last_name"     ) var lastName     : String?  = null,
  @SerializedName("company"       ) var company      : String?  = null,
  @SerializedName("address1"      ) var address1     : String?  = null,
  @SerializedName("address2"      ) var address2     : String?  = null,
  @SerializedName("city"          ) var city         : String?  = null,
  @SerializedName("province"      ) var province     : String?  = null,
  @SerializedName("country"       ) var country      : String?  = null,
  @SerializedName("zip"           ) var zip          : String?  = null,
  @SerializedName("phone"         ) var phone        : String?  = null,
  @SerializedName("name"          ) var name         : String?  = null,
  @SerializedName("province_code" ) var provinceCode : String?  = null,
  @SerializedName("country_code"  ) var countryCode  : String?  = null,
  @SerializedName("country_name"  ) var countryName  : String?  = null,
  @SerializedName("default"       ) var default      : Boolean? = null

)