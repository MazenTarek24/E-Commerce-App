package com.mohamednader.shoponthego.fav

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.LineItems
import com.example.example.SingleProduct

import com.mohamednader.shoponthego.databinding.FavitemBinding


class FavRecycleAdapter(
    private val context: Context, val delete: (Int)->Unit
) : ListAdapter<LineItems, FavRecycleAdapter.ProductViewHolder>(ProductDiff()) {


    lateinit var binding: FavitemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = FavitemBinding.inflate(inflater)
        return ProductViewHolder(binding)

    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem: LineItems = getItem(position)
        println(currentItem.fulfillment_service.toString())
        Glide.with(binding.productPhoto)
            .load(currentItem.fulfillment_service.toString())
            .into(binding.productPhoto)

        holder.binding.productDescription.text = currentItem.sku
        holder.binding.productTitle.text = currentItem.title
    holder.binding.delete.setOnClickListener {
        delete(position)
    }

    }

    inner class ProductViewHolder(var binding: FavitemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class ProductDiff : DiffUtil.ItemCallback<LineItems>() {
    override fun areItemsTheSame(oldItem: LineItems, newItem: LineItems): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LineItems, newItem: LineItems): Boolean {
        return oldItem == newItem
    }

}