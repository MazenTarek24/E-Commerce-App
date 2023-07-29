package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

import com.google.gson.annotations.SerializedName

data class SingleDraftOrderResponse(
        @SerializedName("draft_order") var draftOrder: DraftOrder

)
