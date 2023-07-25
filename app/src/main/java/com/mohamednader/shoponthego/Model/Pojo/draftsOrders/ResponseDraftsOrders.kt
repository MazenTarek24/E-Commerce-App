package com.example.example

import com.google.gson.annotations.SerializedName


data class ResponseDraftsOrders (

  @SerializedName("draft_orders" ) var draftOrders : ArrayList<DraftOrders> = arrayListOf()

)