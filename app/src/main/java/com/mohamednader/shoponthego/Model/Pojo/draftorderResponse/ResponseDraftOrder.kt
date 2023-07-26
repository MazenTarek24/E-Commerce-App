package com.example.example

import com.google.gson.annotations.SerializedName


data class ResponseDraftOrder (

  @SerializedName("draft_order" ) var draftOrder : ResponseDraftOrderOb = ResponseDraftOrderOb()

)