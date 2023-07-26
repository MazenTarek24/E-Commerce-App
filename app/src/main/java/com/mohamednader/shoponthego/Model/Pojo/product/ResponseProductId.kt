package com.example.example

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.Products.Product


data class ResponseProductId (

  @SerializedName("product" ) var product : SingleProduct

)