package com.mohamednader.shoponthego.fav

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItem
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ItemCartBinding

class FavRecycleAdapter(
        private val context: Context, val delete: (LineItem) -> Unit
) : ListAdapter<LineItem, FavRecycleAdapter.ProductViewHolder>(ProductDiff()) {

    lateinit var binding: ItemCartBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemCartBinding.inflate(inflater)
        return ProductViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val currentItem: LineItem = getItem(position)

        binding.itemTitle.text = currentItem.vendor
        binding.itemBrand.text = currentItem.title
        binding.itemPrice.text = currentItem.price
        binding.quantityContainer.visibility = View.GONE

        try {
            Glide.with(context).load(currentItem.properties?.get(0)?.value)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.itemImg)
        } catch (ex: Exception) {
            Glide.with(context).load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.itemImg)
        }


        holder.binding.deleteBtn.setOnClickListener {
            delete(currentItem)
        }

    }

    inner class ProductViewHolder(var binding: ItemCartBinding) :
            RecyclerView.ViewHolder(binding.root)
}

class ProductDiff : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem == newItem
    }

}