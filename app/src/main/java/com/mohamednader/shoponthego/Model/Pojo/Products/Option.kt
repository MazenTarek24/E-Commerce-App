package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class Option(
    val id: Long,
    @SerializedName("product_id")
    val productId: Long,
    val name: String,
    val position: Int,
    val values: List<String>
)

