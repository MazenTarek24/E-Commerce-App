package com.example.example

import com.google.gson.annotations.SerializedName


data class PostDraftOrder (

  @SerializedName("draft_order" ) var draftOrder : DraftOrderPost? = DraftOrderPost()

)