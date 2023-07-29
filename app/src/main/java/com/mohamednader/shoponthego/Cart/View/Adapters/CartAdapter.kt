package com.mohamednader.shoponthego.Cart.View.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItem
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ItemCartBinding

class CartAdapter(private val context: Context,
                  private val productListener: OnProductClickListener,
                  private val plusMinusListener: OnPlusMinusClickListener) :
        ListAdapter<LineItem, CartViewHolder>(CartDiffUtil()) {

    private lateinit var binding: ItemCartBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item: LineItem = getItem(position)

        binding.itemTitle.text = item.vendor
        binding.itemBrand.text = item.title
        binding.itemPrice.text = item.price
        holder.binding.apply {
            binding.tvQuantity.text = item.quantity.toString()
        }

        Log.i("INFO_TAG", "onBindViewHolder: ${item.quantity}")

        try {
            Glide.with(context).load(item.properties?.get(0)?.value!!)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.itemImg)
        } catch (ex: Exception) {
            Glide.with(context).load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.itemImg)
        }

        holder.binding.itemCartCard.setOnClickListener {
            productListener.onProductClickListener(item.productId!!)
        }

        holder.binding.btnPlus.setOnClickListener {
            plusMinusListener.onPlusClickListener(item.id!!)
        }

        holder.binding.btnMinus.setOnClickListener {
            plusMinusListener.onMinusClickListener(item.id!!)
        }

        holder.binding.deleteBtn.setOnClickListener {
            plusMinusListener.onDeleteClickListener(item.id!!)
        }

    }

    fun deleteItem(position: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(position)
        Log.i("INFO_TAG", "deleteItem: ${currentList.toString()}")
        submitList(currentList)
        notifyDataSetChanged()
    }

}



class CartViewHolder(var binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

class CartDiffUtil : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return false
    }
}
