package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Image(
        var id: Long,
        @SerializedName("product_id") var productId: Long,
        var position: Int,
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("updated_at") var updatedAt: String,
        var alt: Any,
        var width: Int,
        var height: Int,
        var src: String,
        @SerializedName("variant_ids") var variantIds: List<Any>,
        @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String
)

