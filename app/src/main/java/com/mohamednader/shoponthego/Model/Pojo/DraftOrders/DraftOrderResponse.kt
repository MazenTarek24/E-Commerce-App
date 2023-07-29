package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName

data class DraftOrderResponse(
        @SerializedName("draft_orders") var draftOrders: List<DraftOrder>
)
