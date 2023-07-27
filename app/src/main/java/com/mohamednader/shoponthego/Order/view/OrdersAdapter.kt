package com.mohamednader.shoponthego.Order.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.databinding.ItemOrderBinding

class OrdersAdapter : ListAdapter<Order, OrdersAdapter.ViewHolder>(BrandDiffUtil()) {

    class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.binding.apply {
            itemDate.text = "created at = ${order.created_at}"
            itemAddress.text = "order number = ${order.number.toString()}"
            itemPhone.text = "name = ${order.billing_address?.firstName} "
            itemId.text = "order id = ${order.id.toString()}"
            itemTotalPriceUsd.text = "total price = ${order.current_total_price.toString()}"
        }
    }

    class BrandDiffUtil : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}

