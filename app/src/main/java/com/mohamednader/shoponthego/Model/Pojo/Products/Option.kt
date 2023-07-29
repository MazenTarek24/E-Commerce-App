package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Option(
        var id: Long,
        @SerializedName("product_id")
        var productId: Long,
        var name: String,
        var position: Int,
        var values: List<String>
)

