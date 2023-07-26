package com.example.example

import com.google.gson.annotations.SerializedName


data class EmailMarketingConsent (

  @SerializedName("state"              ) var state            : String? = null,
  @SerializedName("opt_in_level"       ) var optInLevel       : String? = null,
  @SerializedName("consent_updated_at" ) var consentUpdatedAt : String? = null

)