package com.mohamednader.shoponthego.Model.order

data class Fulfillment(
    val admin_graphql_api_id: String,
    val created_at: String,
    val id: Long,
    val line_items: List<LineItem>,
    val location_id: Long,
    val name: String,
    val order_id: Long,
    val origin_address: OriginAddress,
    val receipt: Receipt,
    val service: String,
    val shipment_status: Any,
    val status: String,
    val tracking_company: String,
    val tracking_number: String,
    val tracking_numbers: List<String>,
    val tracking_url: String,
    val tracking_urls: List<String>,
    val updated_at: String
)