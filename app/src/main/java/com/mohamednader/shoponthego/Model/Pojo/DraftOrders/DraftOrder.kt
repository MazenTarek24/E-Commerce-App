package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer

data class DraftOrder(
        var id: Long? = null,
        var note: String? = null,
        var email: String? = null,
        @SerializedName("taxes_included") var taxesIncluded: Boolean? = null,
        @SerializedName("currency") var currency: String? = null,
        @SerializedName("tax_exempt") var taxExempt: Boolean? = null,
        var name: String? = null,
        var status: String? = null,
        @SerializedName("line_items") var lineItems: MutableList<LineItem>? = null,
        @SerializedName("shipping_address") var shippingAddress: Address? = null,
        @SerializedName("shipping_line") var shippingLine: ShippingLine? = ShippingLine(),
        @SerializedName("billing_address") var billingAddress: Address? = null,
        @SerializedName("invoice_url") var invoiceUrl: String? = null,
        @SerializedName("applied_discount") var appliedDiscount: AppliedDiscount? = null,
        @SerializedName("order_id") var orderId: Long? = null,
        @SerializedName("total_price") var totalPrice: String? = null,
        @SerializedName("subtotal_price") var subtotalPrice: String? = null,
        @SerializedName("total_tax") var totalTax: String? = null,
        var customer: Customer? = null
)