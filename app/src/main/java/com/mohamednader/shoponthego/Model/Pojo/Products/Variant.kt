package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Variant(
    val id: Long,
    @SerializedName("product_id") val productId: Long,
    val title: String,
    val price: String,
    val sku: String,
    val position: Int,
    @SerializedName("inventory_policy") val inventoryPolicy: String,
    @SerializedName("compare_at_price") val compareAtPrice: Any,
    @SerializedName("fulfillment_service") val fulfillmentService: String,
    @SerializedName("inventory_management") val inventoryManagement: String,
    val option1: String,
    val option2: String,
    val option3: Any,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val taxable: Boolean,
    val barcode: Any,
    val grams: Int,
    @SerializedName("image_id") val imageId: Any,
    val weight: Double,
    @SerializedName("weight_unit") val weightUnit: String,
    @SerializedName("inventory_item_id") val inventoryItemId: Long,
    @SerializedName("inventory_quantity") val inventoryQuantity: Int,
    @SerializedName("old_inventory_quantity") val oldInventoryQuantity: Int,
    @SerializedName("requires_shipping") val requiresShipping: Boolean,
    @SerializedName("admin_graphql_api_id") val adminGraphqlApiId: String
)

