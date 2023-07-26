package com.example.example

import com.google.gson.annotations.SerializedName


data class TotalTaxSetmo (

  @SerializedName("shop_money"        ) var shopMoney        : ShopMoneymo?        = ShopMoneymo(),
  @SerializedName("presentment_money" ) var presentmentMoney : PresentmentMoneymo? = PresentmentMoneymo()

)