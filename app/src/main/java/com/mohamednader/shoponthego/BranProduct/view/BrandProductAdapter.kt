package com.mohamednader.shoponthego.BranProduct.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class BrandProductAdapter : ListAdapter<Product , BrandProductAdapter.ViewHolder>(BrandDiffUtil()) {

   public class ViewHolder(val binding : ItemProductBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context) ,
            parent , false ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) {
            Picasso.get().load(product.image.src).into(holder.binding.itemImg)
            holder.binding.titleProduct.text = product.title
            //holder.binding.priceProduct.text = product.variants[0].price
        }
    }


    class BrandDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}