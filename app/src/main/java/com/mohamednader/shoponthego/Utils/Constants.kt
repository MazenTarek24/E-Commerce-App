package com.mohamednader.shoponthego.Utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val shopifyBaseUrl = "https://itp-sv-and2.myshopify.com/admin/api/2023-07/"
    const val currencyBaseUrl = "https://xecdapi.xe.com/v1/"
    const val currencyApiUsername = "shoponthego508887466"
    const val currencyApiPassword = "rob8hkggolci3rva32c60h4mom"
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
    val languageKey = stringPreferencesKey("customer_language_key")
    val currencyKey = stringPreferencesKey("customer_currency_key")

}