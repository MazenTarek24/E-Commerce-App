package com.mohamednader.shoponthego.Cart.View.Adapters

interface OnPlusMinusClickListener {
    fun onPlusClickListener(id: Long)
    fun onMinusClickListener(id: Long)
    fun onDeleteClickListener(id: Long)
}