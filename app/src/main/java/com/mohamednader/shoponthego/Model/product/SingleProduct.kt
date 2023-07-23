package com.example.example

import com.google.gson.annotations.SerializedName


data class SingleProduct (

  @SerializedName("id"                   ) var id                : Long?                = null,
  @SerializedName("title"                ) var title             : String?             = null,
  @SerializedName("body_html"            ) var bodyHtml          : String?             = null,
  @SerializedName("vendor"               ) var vendor            : String?             = null,
  @SerializedName("product_type"         ) var productType       : String?             = null,
  @SerializedName("created_at"           ) var createdAt         : String?             = null,
  @SerializedName("handle"               ) var handle            : String?             = null,
  @SerializedName("updated_at"           ) var updatedAt         : String?             = null,
  @SerializedName("published_at"         ) var publishedAt       : String?             = null,
  @SerializedName("template_suffix"      ) var templateSuffix    : String?             = null,
  @SerializedName("status"               ) var status            : String?             = null,
  @SerializedName("published_scope"      ) var publishedScope    : String?             = null,
  @SerializedName("tags"                 ) var tags              : String?             = null,
  @SerializedName("admin_graphql_api_id" ) var adminGraphqlApiId : String?             = null,
  @SerializedName("variants"             ) var variants          : ArrayList<Variants> = arrayListOf(),
  @SerializedName("options"              ) var options           : ArrayList<Options>  = arrayListOf(),
  @SerializedName("images"               ) var images            : ArrayList<Images>   = arrayListOf(),
  @SerializedName("image"                ) var image             : Image?              = Image()

)