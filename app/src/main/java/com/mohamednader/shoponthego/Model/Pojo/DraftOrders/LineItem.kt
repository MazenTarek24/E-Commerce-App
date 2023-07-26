package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName

data class LineItem(
        val id: Long?,
        @SerializedName("variant_id") val variantId: Long?,
        @SerializedName("product_id") val productId: Long?,
        val title: String?,
        @SerializedName("variant_title") val variantTitle: String?,
        val sku: String?,
        val vendor: String?,
        var quantity: Int?,
        @SerializedName("requires_shipping") val requiresShipping: Boolean?,
        val taxable: Boolean?,
        @SerializedName("gift_card") val giftCard: Boolean?,
        @SerializedName("fulfillment_service") val fulfillmentService: String?,
        val grams: Int?,
        @SerializedName("applied_discount") val appliedDiscount: AppliedDiscount?,
        val name: String?,
        val properties: List<LineItemProperties>,
        val custom: Boolean?,
        val price: String?,
) {
    constructor(variantId: Long, quantity: Int, properties: List<LineItemProperties>) : this(
        null,
        variantId,
        null,
        "",
        null,
        null,
        null,
        quantity,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        properties,
        null,
        null
    )
}
