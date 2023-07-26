package com.mohamednader.shoponthego.Model.Pojo.Customers

import com.google.gson.annotations.SerializedName

data class Address(
    val id: Long,
    @SerializedName("customer_id") val customerId: Long?,
    @SerializedName("first_name") val firstName: Any?,
    @SerializedName("last_name") val lastName: Any?,
    val company: Any?,
    val address1: String,
    val address2: String,
    val city: String,
    val province: String,
    val country: String,
    val zip: String,
    val phone: String,
    val name: String,
    @SerializedName("province_code") val provinceCode: String,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("country_name") val countryName: String,
    val isDefault: Boolean
)
