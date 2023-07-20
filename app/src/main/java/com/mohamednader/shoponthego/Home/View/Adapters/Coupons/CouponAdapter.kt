package com.mohamednader.shoponthego.Home.View.Adapters.Coupons

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Model.Pojo.Coupon.Coupon
import com.mohamednader.shoponthego.databinding.ItemCouponBinding

class CouponAdapter(private val context: Context, private val listener: OnGetNowClickListener) :
    ListAdapter<Coupon, CouponViewHolder>(CouponDiffUtil()) {


    private lateinit var binding: ItemCouponBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        binding = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon: Coupon = getItem(position)

        holder.binding.couponValue.text = coupon.value
        holder.binding.couponDescription.text = coupon.description
        holder.binding.couponCode.text = "with code: ${coupon.code}"

        holder.binding.couponGetNowBtn.setOnClickListener {
            listener.onGetNowClickListener(coupon.code)
        }
    }
}

class CouponViewHolder(var binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root)

class CouponDiffUtil : DiffUtil.ItemCallback<Coupon>() {
    override fun areItemsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
        return oldItem == newItem
    }

}