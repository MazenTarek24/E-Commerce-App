package com.mohamednader.shoponthego.Model.Pojo.Products

import com.google.gson.annotations.SerializedName

data class SingleProductResponse(

        @SerializedName("product") var product: Product

)