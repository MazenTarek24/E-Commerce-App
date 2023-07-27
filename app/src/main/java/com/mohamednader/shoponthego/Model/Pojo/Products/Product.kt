package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Product(
        val id: Long? = null,
        val title: String? = null,
        @SerializedName("body_html") val bodyHtml: String? = null,
        val vendor: String? = null,
        @SerializedName("product_type") val productType: String? = null,
        @SerializedName("created_at") val createdAt: String? = null,
        val handle: String? = null,
        @SerializedName("updated_at") val updatedAt: String? = null,
        @SerializedName("published_at") val publishedAt: String? = null,
        @SerializedName("template_suffix") val templateSuffix: Any? = null,
        val status: String? = null,
        @SerializedName("published_scope") val publishedScope: String? = null,
        val tags: String? = null,
        @SerializedName("admin_graphql_api_id") val adminGraphqlApiId: String? = null,
        val variants: List<Variant>?? = null,
        val options: List<Option>? = null,
        val images: List<Image>? = null,
        val image: Image? = null,
)

