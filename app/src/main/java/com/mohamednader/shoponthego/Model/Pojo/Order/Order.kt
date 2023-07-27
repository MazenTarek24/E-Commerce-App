package com.mohamednader.shoponthego.Model.Pojo.Order

import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import java.io.Serializable

data class Order(

        val billing_address: Address? = null,
        val created_at: String? = null,
        val currency: String? = null,
        val current_subtotal_price: String? = null,
        val current_total_discounts: String? = null,
        val current_total_price: String? = null,
        val current_total_tax: String? = null,
        val customer: Customer? = null,
        val email: String? = null,
        val id: Long? = null,
        val name: String? = null,
        val number: Int? = null,
        val order_number: Int? = null,

        ) : Serializable