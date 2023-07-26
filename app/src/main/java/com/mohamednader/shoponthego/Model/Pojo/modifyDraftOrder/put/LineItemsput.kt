package com.example.example

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItemProperties


data class LineItemsput (

  @SerializedName("id"                   ) var id                 : Long?              = null,
  @SerializedName("product_id"           ) var productId          : Long?              = null,
  @SerializedName("title"                ) var title              : String?           = null,
  @SerializedName("variant_title"        ) var variantTitle       : String?           = null,
  @SerializedName("sku"                  ) var sku                : String?           = null,
  @SerializedName("vendor"               ) var vendor             : String?           = null,
  @SerializedName("quantity"             ) var quantity           : Int?              = null,
  @SerializedName("requires_shipping"    ) var requiresShipping   : Boolean?          = null,
  @SerializedName("taxable"              ) var taxable            : Boolean?          = null,
  @SerializedName("gift_card"            ) var giftCard           : Boolean?          = null,
  @SerializedName("fulfillment_service"  ) var fulfillmentService : String?           = null,
  @SerializedName("grams"                ) var grams              : Int?              = null,
  @SerializedName("tax_lines"            ) var taxLines           : ArrayList<String> = arrayListOf(),
  @SerializedName("applied_discount"     ) var appliedDiscount    : String?           = null,
  @SerializedName("name"                 ) var name               : String?           = null,
  @SerializedName("properties"           ) var properties         : ArrayList<LineItemProperties> = arrayListOf(),
  @SerializedName("custom"               ) var custom             : Boolean?          = null,
  @SerializedName("price"                ) var price              : String?           = null,
  @SerializedName("admin_graphql_api_id" ) var adminGraphqlApiId  : String?           = null

)