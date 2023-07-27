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
import com.mohamednader.shoponthego.R

import com.mohamednader.shoponthego.databinding.FavitemBinding


class FavRecycleAdapter(
    private val context: Context, val delete: (LineItems)->Unit
) : ListAdapter<LineItems, FavRecycleAdapter.ProductViewHolder>(ProductDiff()) {


    lateinit var binding: FavitemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = FavitemBinding.inflate(inflater)
        return ProductViewHolder(binding)

    }


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if(position+1>0) {

            val currentItem: LineItems = getItem(position)
            try {
                Glide.with(context).load(currentItem.properties?.get(0)?.value)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.binding.productPhoto)
            } catch (ex: Exception) {
                Glide.with(context).load(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.binding.productPhoto)
            }

            holder.binding.productDescription.text = currentItem.sku
            holder.binding.productTitle.text = currentItem.title
            holder.binding.delete.setOnClickListener {
                delete(currentItem)
            }
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