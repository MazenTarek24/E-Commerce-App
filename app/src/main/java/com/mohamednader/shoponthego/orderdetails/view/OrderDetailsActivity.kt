package com.mohamednader.shoponthego.orderdetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater

import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mohamednader.shoponthego.Model.order.Order
import com.mohamednader.shoponthego.Model.order.OrderX


import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ActivityOrderBinding
import com.mohamednader.shoponthego.databinding.ActivityOrderDetailsBinding


class OrderDetailsActivity : AppCompatActivity() {


    lateinit var orderAdapterDetails : OrdersAdapterDetails
    lateinit var layoutManager: LayoutManager

    lateinit var binding : ActivityOrderDetailsBinding


    private val TAG = "OrderActivityDetails_INFO_TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

       val  selectOrder : OrderX? = intent.getSerializableExtra("ORDER_OBJECT") as OrderX
        if (selectOrder!=null)
        {
            showOrderDetails(selectOrder)
        }

    }

    private fun showOrderDetails(order: OrderX) {
        binding.itemTitle.text = order.name
        binding.itemTotalPriceUsd.text = order.total_price_usd
    }

}