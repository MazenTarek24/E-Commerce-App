package com.mohamednader.shoponthego.Model.Pojo.DraftOrders

/*

"title": "title of the discount",
      "description": "Description of discount!",
      "value": "10",
      "value_type": "percentage",
      "amount": "19.99"

 */

data class AppliedDiscount(var description: String = "",
                           var value: String = "",
                           var title: String = " ",
                           var amount: String = "",
                           var value_type: String = "")