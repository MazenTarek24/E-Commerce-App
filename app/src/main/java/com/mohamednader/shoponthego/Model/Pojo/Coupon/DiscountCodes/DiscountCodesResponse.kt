package com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes

import com.google.gson.annotations.SerializedName

data class DiscountCodesResponse(
        @SerializedName("discount_codes") var discountCodesList: List<DiscountCodes>,
)
