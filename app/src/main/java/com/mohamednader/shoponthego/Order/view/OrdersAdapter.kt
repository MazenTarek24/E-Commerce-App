package com.mohamednader.shoponthego.Order.view

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Utils.convertCurrencyFromEGPTo
import com.mohamednader.shoponthego.databinding.ItemOrderBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class OrdersAdapter(val currencyRate: Double , val currencyISO: String) : ListAdapter<Order, OrdersAdapter.ViewHolder>(BrandDiffUtil()) {

    class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.binding.apply {

            val timestamp = order.created_at
            val formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm a")
            val zonedDateTime = ZonedDateTime.parse(timestamp)
            val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
            val formattedDateTime = localDateTime.format(formatter)

            itemDate.text = "created at = ${formattedDateTime}"
            itemAddress.text = "order number = ${order.number.toString()}"
            itemPhone.text = "name = ${order.billing_address?.firstName} "
            itemId.text = "order id = ${order.id.toString()}"

            itemTotalPriceUsd.text = "Total price = ${
                convertCurrencyFromEGPTo((order.current_total_price)!!.toDouble(),
                        currencyRate)
            } $currencyISO"

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

