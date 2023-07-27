package com.mohamednader.shoponthego.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.R

class MyListAdapter(
        private val context: Context, val Open: (Product) -> Unit
) : ListAdapter<Product, MyListAdapter.MyViewHolder>(StringDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_itemsearch, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.title!!)
        holder.layout.setOnClickListener {
            Open(item)
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.itemText)
        val layout: ConstraintLayout = itemView.findViewById(R.id.layout)

        fun bind(item: String) {
            itemText.text = item
        }

    }

    private class StringDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}

//class MyListAdapter(
//private val context: Context, val open: (Product)->Unit
//) : ListAdapter<Product, MyListAdapter.ProductViewHolder>(ProductDiff()) {
//
//
//    lateinit var binding: ListItemsearchBinding
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        binding = ListItemsearchBinding.inflate(inflater)
//        return ProductViewHolder(binding)
//
//    }
//
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val currentItem: Product = getItem(position)
//    holder.binding.itemText.text=currentItem.title
//
//        holder.binding.layout.setOnClickListener {
//            open(currentItem)
//        }
//
//    }
//
//    inner class ProductViewHolder(var binding: ListItemsearchBinding) :
//        RecyclerView.ViewHolder(binding.root)
//}
//
//class ProductDiff : DiffUtil.ItemCallback<Product>() {
//    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return oldItem == newItem
//    }
//
//}