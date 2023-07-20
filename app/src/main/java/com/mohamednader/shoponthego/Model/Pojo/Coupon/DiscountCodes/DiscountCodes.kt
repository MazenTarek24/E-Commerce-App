package com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes

import com.google.gson.annotations.SerializedName
data class DiscountCodes(
    val id : Long,
    @SerializedName("price_rule_id") val priceRuleId: Long,
    val code: String,
    @SerializedName("usage_count") val usageCount: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
    )
