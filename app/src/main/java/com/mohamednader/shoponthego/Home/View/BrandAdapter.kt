package com.mohamednader.shoponthego.Home.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.databinding.ItemBrandBinding
import com.squareup.picasso.Picasso

class BrandAdapter() : ListAdapter<SmartCollection , BrandAdapter.ViewHolder>(BrandDiffUtil()) {


    class ViewHolder(val binding : ItemBrandBinding) : RecyclerView.ViewHolder(binding.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBrandBinding.inflate(LayoutInflater.from(parent.context),
            parent , false ) )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBrand = getItem(position)
        Picasso.get().load(currentBrand.image.src).into(holder.binding.brandImage)
        holder.binding.brandTitle.text = currentBrand.title

        val brandString = currentBrand.id.toString()
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragment2ToBrandProductFragment(brandString)
            it.findNavController().navigate(action)
        }
    }

}

class BrandDiffUtil : DiffUtil.ItemCallback<SmartCollection>() {
    override fun areItemsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
        return oldItem == newItem
    }

}
