package com.mohamednader.shoponthego.Home.View.Adapters.Coupons

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.databinding.ItemCurrencyBinding

class CurrencyAdapter(private val context: Context, private val listener: OnCurrencyClickListener) :
    ListAdapter<CurrencyInfo, CurrencyViewHolder>(CurrencyDiffUtil()) {


    private lateinit var binding: ItemCurrencyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency: CurrencyInfo = getItem(position)

        holder.binding.currencyIso.text = currency.iso
        holder.binding.currencyName.text = currency.currencyName

        holder.binding.root.setOnClickListener {
            listener.onCurrencyClickListener(currency.iso)
        }
    }
}

class CurrencyViewHolder(var binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root)

class CurrencyDiffUtil : DiffUtil.ItemCallback<CurrencyInfo>() {
    override fun areItemsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo): Boolean {
        return oldItem == newItem
    }

}