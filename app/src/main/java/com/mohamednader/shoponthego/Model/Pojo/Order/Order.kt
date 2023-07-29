package com.mohamednader.shoponthego.Model.Pojo.Order

import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import java.io.Serializable

data class Order(

        var billing_address: Address? = null,
        var created_at: String? = null,
        var currency: String? = null,
        var current_subtotal_price: String? = null,
        var current_total_discounts: String? = null,
        var current_total_price: String? = null,
        var current_total_tax: String? = null,
        var customer: Customer? = null,
        var email: String? = null,
        var id: Long? = null,
        var name: String? = null,
        var number: Int? = null,
        var order_number: Int? = null,

        ) : Serializable