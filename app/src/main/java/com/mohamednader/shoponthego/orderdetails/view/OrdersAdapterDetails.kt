package com.mohamednader.shoponthego.orderdetails.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.LineItems
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Model.order.OrderX
import com.mohamednader.shoponthego.databinding.ItemBrandBinding
import com.mohamednader.shoponthego.databinding.ItemOrderBinding
import com.mohamednader.shoponthego.databinding.ItemOrderDetailsBinding

class OrdersAdapterDetails() : ListAdapter<OrderX , OrdersAdapterDetails.ViewHolder> (BrandDiffUtil()){


    class ViewHolder(val binding : ItemOrderDetailsBinding) : RecyclerView.ViewHolder(binding.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderDetailsBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.binding.apply {
            itemTitle.text = order.name
            itemTotalPriceUsd.text = order.total_price_usd
        }

    }

    class BrandDiffUtil : DiffUtil.ItemCallback<OrderX>() {
        override fun areItemsTheSame(oldItem: OrderX, newItem: OrderX): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: OrderX, newItem: OrderX): Boolean {
            return oldItem == newItem
        }
    }
}