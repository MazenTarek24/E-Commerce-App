package com.mohamednader.shoponthego.Cart.View.Adapters

interface OnPlusMinusClickListener {
    fun onPlusClickListener(productVariantId: Long)
    fun onMinusClickListener(productVariantId: Long)
}