package com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules

import com.google.gson.annotations.SerializedName

data class PriceRules(
        var id: Long,
        @SerializedName("value_type") var valueType: String,
        var value: String,
        @SerializedName("target_type") var targetType: String,
        var title: String
)
