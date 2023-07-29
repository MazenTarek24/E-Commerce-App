package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName

data class LineItem(
        var id: Long? = null,
        @SerializedName("variant_id") var variantId: Long? = null,
        @SerializedName("product_id") var productId: Long? = null,
        var title: String? = null,
        @SerializedName("variant_title") var variantTitle: String? = null,
        var sku: String? = null,
        var vendor: String? = null,
        var quantity: Int? = null,
        @SerializedName("requires_shipping") var requiresShipping: Boolean? = null,
        var taxable: Boolean? = null,
        @SerializedName("gift_card") var giftCard: Boolean? = null,
        @SerializedName("fulfillment_service") var fulfillmentService: String? = null,
        var grams: Int? = null,
        @SerializedName("applied_discount") var appliedDiscount: AppliedDiscount? = null,
        var name: String? = null,
        var properties: List<LineItemProperties>? = null,
        var custom: Boolean? = null,
        var price: String? = null,
)