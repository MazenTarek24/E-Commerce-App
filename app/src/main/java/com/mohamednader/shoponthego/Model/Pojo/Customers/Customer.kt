package com.mohamednader.shoponthego.Model.Pojo.Customers

import com.google.gson.annotations.SerializedName

data class Customer(var id: Long?,
                    var email: String?,
                    @SerializedName("first_name") var firstName: String?,
                    @SerializedName("last_name") var lastName: String?,
                    @SerializedName("orders_count") var ordersCount: Int?,
                    var state: String?,
                    @SerializedName("total_spent") var totalSpent: String?,
                    @SerializedName("last_order_id") var lastOrderId: Long?,
                    var note: String?,
                    var verifiedEmail: Boolean?,
                    @SerializedName("last_order_name") var lastOrderName: String?,
                    var currency: String?,
                    @SerializedName("phone") var phone: String?,
                    var addresses: MutableList<Address>?,
                    @SerializedName("default_address") var defaultAddress: Address?

) {
    constructor(id: Long) : this(id,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null)

    constructor(firstName: String?, note: String?, verifiedEmail: Boolean?, email: String?) : this(
        null,
        email,
        firstName,
        null,
        null,
        null,
        null,
        null,
        note,
        verifiedEmail,
        null,
        null,
        null,
        null,
        null)

    constructor() : this(null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null)
}
