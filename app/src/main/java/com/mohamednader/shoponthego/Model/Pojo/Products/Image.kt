package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Image(
    val id: Long,
    @SerializedName("product_id") val productId: Long,
    val position: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val alt: Any,
    val width: Int,
    val height: Int,
    val src: String,
    @SerializedName("variant_ids") val variantIds: List<Any>,
    @SerializedName("admin_graphql_api_id") val adminGraphqlApiId: String
)

