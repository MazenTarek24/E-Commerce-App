package com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules

import com.google.gson.annotations.SerializedName

data class PriceRules(
    val id: Long,
    @SerializedName("value_type") val valueType: String,
    val value: String,
    @SerializedName("target_type") val targetType: String,
    val title: String
)
