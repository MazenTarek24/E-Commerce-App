package com.mohamednader.shoponthego.Model.order

import android.accessibilityservice.AccessibilityGestureEvent.CREATOR
import android.accessibilityservice.AccessibilityServiceInfo.CREATOR
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

data class OrderX(
    val billing_address: BillingAddress?= null,
    val contact_email: String?= null,
    val created_at: String?= null,
    val currency: String?= null,
    val current_total_discounts: String?= null,
    val current_total_duties_set: Any?= null,
    val current_total_price: String?= null,
    val customer: Customer?= null,
    val device_id: Any?,
    val email: String?= null,
    val id: Long?= null,
    val line_items: List<LineItem>?= null,
    val name: String?= null,
    val order_number: Int?= null,
    val number: Int? = null,
    val total_discounts: String?= null,
    val total_price: String?= null,
    val total_price_usd: String?= null,
    val total_tax: String?= null,
    val user_id: Any ?= null,
) : Serializable