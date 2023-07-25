package com.mohamednader.shoponthego.Model.Pojo

import com.mohamednader.shoponthego.Model.Pojo.customer.Customer

data class DraftOrderResponse(
    var draft_order: DraftOrder = DraftOrder()
)

data class DraftOrder(
    val id: Long? = null,
    val note: String? = null,
    val email: String? = null,
    val taxes_included: Boolean? = null,
    val currency: String? = null,
    val invoice_sent_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val tax_exempt: Boolean? = null,
    val completed_at: String? = null,
    val name: String? = null,
    val status: String? = null,
    var line_items: List<LineItems>? = null,
    val shipping_address: ShippingAddress? = null,
    val billing_address: BillingAddress? = null,
    val invoice_url: String? = null,
    var applied_discount: AppliedDiscount? = null,
    val order_id: String? = null,
    val shipping_line: ShippingLine? = null,
    val tax_lines: List<TaxLine>? = null,
    val tags: String? = null,
    val note_attributes: List<String>? = null,
    val total_price: String? = null,
    val subtotal_price: String? = null,
    val total_tax: String? = null,
    val payment_terms: String? = null,
    val admin_graphql_api_id: String? = null,
    val customer: Customer? = null
)

data class LineItems(
    var id: Long? = null,
    var variant_id: Long? = null,
    var product_id: Long? = null,
    var title: String? = null,
    var variant_title: String? = null,
    var sku: String? = null,
    var vendor: String? = null,
    var quantity: Int? = null,
    var requires_shipping: Boolean? = null,
    var taxable: Boolean? = null,
    var gift_card: Boolean? = null,
    var fulfillment_service: String? = null,
    var grams: Int? = null,
    var tax_lines: List<TaxLine>? = null,
    var applied_discount: AppliedDiscount? = null,
    var name: String? = null,
    var properties: List<Any>? = null,
    var custom: Boolean? = null,
    var price: String? = null,
    var admin_graphql_api_id: String? = null
)

data class ShippingAddress(
    val first_name: String? = null,
    val address1: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val zip: String? = null,
    val province: String? = null,
    val country: String? = null,
    val last_name: String? = null,
    val address2: String? = null,
    val company: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val country_code: String? = null,
    val province_code: String? = null
)

data class BillingAddress(
    val first_name: String? = null,
    val address1: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val zip: String? = null,
    val province: String? = null,
    val country: String? = null,
    val last_name: String? = null,
    val address2: String? = null,
    val company: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val country_code: String? = null,
    val province_code: String? = null
)

data class AppliedDiscount(
    var description: String? = null,
    var value: String? = null,
    var title: String? = null,
    var amount: String? = null,
    var value_type: String? = null
)

data class ShippingLine(
    val title: String? = null,
    val custom: Boolean? = null,
    val handle: String? = null,
    val price: String? = null
)

data class TaxLine(
    val title: String? = null,
    val price: String? = null
)