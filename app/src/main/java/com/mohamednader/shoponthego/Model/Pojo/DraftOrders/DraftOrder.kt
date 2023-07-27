package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer

data class DraftOrder(
        val id: Long? = null,
        var note: String? = null,
        val email: String? = null,
        @SerializedName("taxes_included") val taxesIncluded: Boolean? = null,
        @SerializedName("currency") val currency: String? = null,
        @SerializedName("tax_exempt") val taxExempt: Boolean? = null,
        val name: String? = null,
        val status: String? = null,
        @SerializedName("line_items") val lineItems: MutableList<LineItem>? = null,
        @SerializedName("shipping_address") val shippingAddress: Address? = null,
        @SerializedName("billing_address") val billingAddress: Address? = null,
        @SerializedName("invoice_url") val invoiceUrl: String? = null,
        @SerializedName("applied_discount") val appliedDiscount: AppliedDiscount? = null,
        @SerializedName("order_id") val orderId: Long? = null,
        @SerializedName("total_price") val totalPrice: String? = null,
        @SerializedName("subtotal_price") val subtotalPrice: String? = null,
        @SerializedName("total_tax") val totalTax: String? = null,
        val customer: Customer? = null
)