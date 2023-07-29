package com.mohamednader.shoponthego.Utils

fun convertCurrencyFromEGPTo(productPrice: Double, exchangeRate: Double): String {
    val result = String.format("%.2f", productPrice * exchangeRate).toDouble()
    return (result).toString()
}



