package com.mohamednader.shoponthego.Model.Pojo.Customers

import com.google.gson.annotations.SerializedName

data class Customer(
        var id: Long?,
        val email: String?,
        @SerializedName("first_name") val firstName: String?,
        @SerializedName("last_name") val lastName: String?,
        @SerializedName("orders_count") val ordersCount: Int?,
        val state: String?,
        @SerializedName("total_spent") val totalSpent: String?,
        @SerializedName("last_order_id") val lastOrderId: Long?,
        val note: String?,
        val verifiedEmail: Boolean?,
        @SerializedName("last_order_name") val lastOrderName: String?,
        val currency: String?,
        @SerializedName("phone") val phone: String?,
        val addresses: List<Address>?,
        @SerializedName("default_address") val defaultAddress: Address?


) {
    constructor(id: Long) : this(id, null, null, null, null, null, null, null, null, null, null, null, null, null, null )
}
