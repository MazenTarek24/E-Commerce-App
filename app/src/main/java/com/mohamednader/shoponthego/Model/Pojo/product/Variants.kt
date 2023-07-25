package com.example.example

import com.google.gson.annotations.SerializedName


data class Variants (

  @SerializedName("id"                     ) var id                   : Long?                         = null,
  @SerializedName("product_id"             ) var productId            : Long?                         = null,
  @SerializedName("title"                  ) var title                : String?                      = null,
  @SerializedName("price"                  ) var price                : String?                      = null,
  @SerializedName("sku"                    ) var sku                  : String?                      = null,
  @SerializedName("position"               ) var position             : Int?                         = null,
  @SerializedName("inventory_policy"       ) var inventoryPolicy      : String?                      = null,
  @SerializedName("compare_at_price"       ) var compareAtPrice       : String?                      = null,
  @SerializedName("fulfillment_service"    ) var fulfillmentService   : String?                      = null,
  @SerializedName("inventory_management"   ) var inventoryManagement  : String?                      = null,
  @SerializedName("option1"                ) var option1              : String?                      = null,
  @SerializedName("option2"                ) var option2              : String?                      = null,
  @SerializedName("option3"                ) var option3              : String?                      = null,
  @SerializedName("created_at"             ) var createdAt            : String?                      = null,
  @SerializedName("updated_at"             ) var updatedAt            : String?                      = null,
  @SerializedName("taxable"                ) var taxable              : Boolean?                     = null,
  @SerializedName("barcode"                ) var barcode              : String?                      = null,
  @SerializedName("grams"                  ) var grams                : Long?                         = null,
  @SerializedName("image_id"               ) var imageId              : Int?                         = null,
  @SerializedName("weight"                 ) var weight               : Double?                      = null,
  @SerializedName("weight_unit"            ) var weightUnit           : String?                      = null,
  @SerializedName("inventory_item_id"      ) var inventoryItemId      : Long?                         = null,
  @SerializedName("inventory_quantity"     ) var inventoryQuantity    : Long?                         = null,
  @SerializedName("old_inventory_quantity" ) var oldInventoryQuantity : Long?                         = null,
  @SerializedName("presentment_prices"     ) var presentmentPrices    : ArrayList<PresentmentPrices> = arrayListOf(),
  @SerializedName("requires_shipping"      ) var requiresShipping     : Boolean?                     = null,
  @SerializedName("admin_graphql_api_id"   ) var adminGraphqlApiId    : String?                      = null

)