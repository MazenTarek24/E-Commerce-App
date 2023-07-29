package com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes

import com.google.gson.annotations.SerializedName

data class DiscountCodes(
        var id: Long,
        @SerializedName("price_rule_id") var priceRuleId: Long,
        var code: String,
        @SerializedName("usage_count") var usageCount: Int,
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("updated_at") var updatedAt: String
)
