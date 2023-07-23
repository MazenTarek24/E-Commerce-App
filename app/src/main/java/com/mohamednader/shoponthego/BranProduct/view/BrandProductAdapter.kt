package com.mohamednader.shoponthego.BranProduct.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.databinding.ItemProductBinding
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class BrandProductAdapter: ListAdapter<Product , BrandProductAdapter.ViewHolder>(BrandDiffUtil()) {

   public class ViewHolder(val binding : ItemProductBinding) : RecyclerView.ViewHolder(binding.root){

    }
    private var productsBrand: List<Product> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context) ,
            parent , false ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) {
            Picasso.get().load(product.image.src).into(holder.binding.itemImg)
            holder.binding.titleProduct.text = product.productType

            var price = product.variants?.getOrNull(0)?.price ?: 1.0
            holder.binding.priceProduct.text = price.toString()

//            holder.binding.priceProduct.text = product.vendor
        }
    }

    fun deleteProductBrand(){
        productsBrand = listOf()
        notifyDataSetChanged()
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