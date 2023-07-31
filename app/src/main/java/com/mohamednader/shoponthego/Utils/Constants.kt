package com.mohamednader.shoponthego.Utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val shopifyBaseUrl = "https://itp-sv-and2.myshopify.com/admin/api/2023-07/"
    const val currencyBaseUrl = "https://xecdapi.xe.com/v1/"
    const val currencyApiUsername = "shoponthego239952826"
    const val currencyApiPassword = "8uqio9c6ce86atj80k82942b19"
    const val SIZES = "sizes"
    const val SMALL = "S"
    const val LARGE = "L"
    const val XLARGE = "XL"
    const val MEDIUM = "M"
    const val COLORS = "colors"
    const val GREEN = "green"
    const val RED = "red"
    const val PURPLE = "purple"
    const val BLACK = "black"
    const val ORANGE = "orange"

    const val COLORS_TYPE = "colors"
    const val SIZES_TYPE = "sizes"

    val customerIdKey = stringPreferencesKey("customer_id_key")
    val firstTimeKey = stringPreferencesKey("first_time")
    val isGuestUser = stringPreferencesKey("is_guest_user")
    val languageKey = stringPreferencesKey("customer_language_key")
    val currencyKey = stringPreferencesKey("customer_currency_key")
    val rateKey = stringPreferencesKey("customer_rate_key")

}