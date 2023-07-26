package com.mohamednader.shoponthego.Profile.View.Addresses

interface OnAddressClickListener {
    fun onAddressClickListener(addressId: Long)
    fun onMakeDefaultClickListener(addressId: Long)
    fun onDeleteClickListener(addressId: Long)
}