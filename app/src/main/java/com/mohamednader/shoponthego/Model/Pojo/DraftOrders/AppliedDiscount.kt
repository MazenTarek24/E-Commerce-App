package com.mohamednader.shoponthego.Model.Pojo.DraftOrders


/*

"title": "title of the discount",
      "description": "Description of discount!",
      "value": "10",
      "value_type": "percentage",
      "amount": "19.99"

 */

data class AppliedDiscount(
    val title: String,
    val description: String,
    val value: String,
    val value_type: String,
    val amount: String
)