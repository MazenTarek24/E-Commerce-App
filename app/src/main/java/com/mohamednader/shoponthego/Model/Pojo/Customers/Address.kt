package com.mohamednader.shoponthego.Model.Pojo.Customers

import com.google.gson.annotations.SerializedName

data class Address(
        var id: Long? = null,
        @SerializedName("customer_id") var customerId: Long? = null,
        @SerializedName("first_name") var firstName: String? = null,
        @SerializedName("last_name") var lastName: String? = null,
        var company: Any? = null,
        var address1: String? = null,
        var address2: String? = null,
        var city: String? = null,
        var province: String? = null,
        var country: String? = null,
        var zip: String? = null,
        var phone: String? = null,
        var name: String? = null,
        @SerializedName("province_code") var provinceCode: String? = null,
        @SerializedName("country_code") var countryCode: String? = null,
        @SerializedName("country_name") var countryName: String? = null,
        var default: Boolean? = false
) : java.io.Serializable
