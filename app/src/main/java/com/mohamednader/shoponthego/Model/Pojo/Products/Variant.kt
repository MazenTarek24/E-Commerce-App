package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Variant(
        var id: Long,
        @SerializedName("product_id") var productId: Long,
        var title: String,
        var price: String,
        var sku: String,
        var position: Int,
        @SerializedName("inventory_policy") var inventoryPolicy: String,
        @SerializedName("compare_at_price") var compareAtPrice: Any,
        @SerializedName("fulfillment_service") var fulfillmentService: String,
        @SerializedName("inventory_management") var inventoryManagement: String,
        var option1: String,
        var option2: String,
        var option3: Any,
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("updated_at") var updatedAt: String,
        var taxable: Boolean,
        var barcode: Any,
        var grams: Int,
        @SerializedName("image_id") var imageId: Any,
        var weight: Double,
        @SerializedName("weight_unit") var weightUnit: String,
        @SerializedName("inventory_item_id") var inventoryItemId: Long,
        @SerializedName("inventory_quantity") var inventoryQuantity: Int,
        @SerializedName("old_inventory_quantity") var oldInventoryQuantity: Int,
        @SerializedName("requires_shipping") var requiresShipping: Boolean,
        @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String
)

