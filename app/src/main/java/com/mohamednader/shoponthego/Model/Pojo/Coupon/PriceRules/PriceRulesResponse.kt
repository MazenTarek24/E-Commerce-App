package com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules

import com.google.gson.annotations.SerializedName

data class PriceRulesResponse(
        @SerializedName("price_rules") var priceRulesList: List<PriceRules>,

        )
