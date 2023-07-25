package com.example.example

import com.google.gson.annotations.SerializedName


data class Customerre (

    @SerializedName("id"                           ) var id                        : Long?                   = null,
    @SerializedName("email"                        ) var email                     : String?                = null,
    @SerializedName("accepts_marketing"            ) var acceptsMarketing          : Boolean?               = null,
    @SerializedName("created_at"                   ) var createdAt                 : String?                = null,
    @SerializedName("updated_at"                   ) var updatedAt                 : String?                = null,
    @SerializedName("first_name"                   ) var firstName                 : String?                = null,
    @SerializedName("last_name"                    ) var lastName                  : String?                = null,
    @SerializedName("orders_count"                 ) var ordersCount               : Int?                   = null,
    @SerializedName("state"                        ) var state                     : String?                = null,
    @SerializedName("total_spent"                  ) var totalSpent                : String?                = null,
    @SerializedName("last_order_id"                ) var lastOrderId               : String?                = null,
    @SerializedName("note"                         ) var note                      : String?                = null,
    @SerializedName("verified_email"               ) var verifiedEmail             : Boolean?               = null,
    @SerializedName("multipass_identifier"         ) var multipassIdentifier       : String?                = null,
    @SerializedName("tax_exempt"                   ) var taxExempt                 : Boolean?               = null,
    @SerializedName("tags"                         ) var tags                      : String?                = null,
    @SerializedName("last_order_name"              ) var lastOrderName             : String?                = null,
    @SerializedName("currency"                     ) var currency                  : String?                = null,
    @SerializedName("phone"                        ) var phone                     : String?                = null,
    @SerializedName("addresses"                    ) var addresses                 : ArrayList<AddressesCustomers>   = arrayListOf(),
    @SerializedName("accepts_marketing_updated_at" ) var acceptsMarketingUpdatedAt : String?                = null,
    @SerializedName("marketing_opt_in_level"       ) var marketingOptInLevel       : String?                = null,
    @SerializedName("tax_exemptions"               ) var taxExemptions             : ArrayList<String>      = arrayListOf(),
    @SerializedName("email_marketing_consent"      ) var emailMarketingConsent     : EmailMarketingConsentResponse? = EmailMarketingConsentResponse(),
    @SerializedName("sms_marketing_consent"        ) var smsMarketingConsent       : SmsMarketingConsentResponse?   = SmsMarketingConsentResponse(),
    @SerializedName("admin_graphql_api_id"         ) var adminGraphqlApiId         : String?                = null,
    @SerializedName("default_address"              ) var defaultAddress            : DefaultAddressCus?        = DefaultAddressCus()

)