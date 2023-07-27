package com.mohamednader.shoponthego.Model.Pojo.Customers

import com.google.gson.annotations.SerializedName

data class Address(
        val id: Long?  = null,
        @SerializedName("customer_id") val customerId: Long? = null,
        @SerializedName("first_name") val firstName: String? = null,
        @SerializedName("last_name") val lastName: String? = null,
        val company: Any? = null,
        val address1: String? = null,
        var address2: String? = null,
        val city: String? = null,
        val province: String? = null,
        val country: String? = null,
        val zip: String? = null,
        val phone: String? = null,
        val name: String? = null,
        @SerializedName("province_code") val provinceCode: String? = null,
        @SerializedName("country_code") val countryCode: String? = null,
        @SerializedName("country_name") val countryName: String? = null,
        var default: Boolean? = false
): java.io.Serializable
