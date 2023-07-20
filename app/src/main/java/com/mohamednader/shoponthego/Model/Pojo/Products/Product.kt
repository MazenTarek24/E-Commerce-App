package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Long,
    val title: String,
    @SerializedName("body_html") val bodyHtml: String,
    val vendor: String,
    @SerializedName("product_type") val productType: String,
    @SerializedName("created_at") val createdAt: String,
    val handle: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("template_suffix") val templateSuffix: Any,
    val status: String,
    @SerializedName("published_scope") val publishedScope: String,
    val tags: String,
    @SerializedName("admin_graphql_api_id") val adminGraphqlApiId: String,
    val variants: List<Variant> ,
    val options: List<Option>,
    val images: List<Image>,
    val image: Image
)

