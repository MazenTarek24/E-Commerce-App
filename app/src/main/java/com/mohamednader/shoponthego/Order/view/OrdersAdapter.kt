package com.mohamednader.shoponthego.Order.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.order.OrderX
import com.mohamednader.shoponthego.databinding.ItemOrderBinding

class OrdersAdapter() : ListAdapter<OrderX , OrdersAdapter.ViewHolder> (BrandDiffUtil()) {


    class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.binding.apply {
            itemDate.text = order.created_at
            itemAddress.text = order.billing_address.toString()
            itemId.text = order.id.toString()
            itemTotalPriceUsd.text = order.total_price.toString()
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

