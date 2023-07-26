package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer

data class DraftOrder(
        val id: Long?,
        var note: String?,
        val email: String?,
        @SerializedName("taxes_included") val taxesIncluded: Boolean?,
        @SerializedName("currency") val currency: String?,
        @SerializedName("tax_exempt") val taxExempt: Boolean?,
        val name: String?,
        val status: String?,
        @SerializedName("line_items") val lineItems: MutableList<LineItem>?,
        @SerializedName("shipping_address") val shippingAddress: Address?,
        @SerializedName("billing_address") val billingAddress: Address?,
        @SerializedName("invoice_url") val invoiceUrl: String?,
        @SerializedName("applied_discount") val appliedDiscount: AppliedDiscount?,
        @SerializedName("order_id") val orderId: Long?,
        @SerializedName("total_price") val totalPrice: String?,
        @SerializedName("subtotal_price") val subtotalPrice: String?,
        @SerializedName("total_tax") val totalTax: String?,
        val customer: Customer?
) {
    constructor(lineItems: MutableList<LineItem>, customer: Customer, note: String) : this(null,
            note,
            null,
            null,
            null,
            null,
            null,
            null,
            lineItems,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            customer)
}
