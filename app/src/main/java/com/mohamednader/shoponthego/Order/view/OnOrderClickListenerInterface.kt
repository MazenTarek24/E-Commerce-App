package com.mohamednader.shoponthego.Order.view

import com.mohamednader.shoponthego.Model.order.Order
import com.mohamednader.shoponthego.Model.order.OrderX

interface OnOrderClickListenerInterface {
    fun onOrderClickListener(order: OrderX)
}