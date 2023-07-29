package com.mohamednader.shoponthego.Model.order

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.AppliedDiscount
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItemProperties

data class LineItem(
    val id: Long? = null,
    @SerializedName("variant_id") val variantId: Long? = null,
    @SerializedName("product_id") val productId: Long? = null,
    val title: String? = null,
    @SerializedName("variant_title") val variantTitle: String? = null,
    val sku: String? = null,
    val vendor: String? = null,
    var quantity: Int? = null,
    @SerializedName("requires_shipping") val requiresShipping: Boolean? = null,
    val taxable: Boolean? = null,
    @SerializedName("gift_card") val giftCard: Boolean? = null,
    @SerializedName("fulfillment_service") val fulfillmentService: String? = null,
    val grams: Int? = null,
    @SerializedName("applied_discount") val appliedDiscount: AppliedDiscount? = null,
    val name: String? = null,
    val properties: List<LineItemProperties>? = null,
    val custom: Boolean? = null,
    val price: String? = null,
)