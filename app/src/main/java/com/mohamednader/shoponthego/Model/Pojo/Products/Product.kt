package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Product(
        var id: Long? = null,
        var title: String? = null,
        @SerializedName("body_html") var bodyHtml: String? = null,
        var vendor: String? = null,
        @SerializedName("product_type") var productType: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        var handle: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null,
        @SerializedName("published_at") var publishedAt: String? = null,
        @SerializedName("template_suffix") var templateSuffix: Any? = null,
        var status: String? = null,
        @SerializedName("published_scope") var publishedScope: String? = null,
        var tags: String? = null,
        @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String? = null,
        var variants: List<Variant>?? = null,
        var options: List<Option>? = null,
        var images: List<Image>? = null,
        var image: Image? = null,
)

