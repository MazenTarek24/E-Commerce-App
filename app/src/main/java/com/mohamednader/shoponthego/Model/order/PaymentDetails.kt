package com.mohamednader.shoponthego.Model.order

data class PaymentDetails(
    val avs_result_code: Any,
    val buyer_action_info: Any,
    val credit_card_bin: Any,
    val credit_card_company: String,
    val credit_card_number: String,
    val cvv_result_code: Any
)