package com.mohamednader.shoponthego.productinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.R


class ReviewAdapter(private val reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemreview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]
        holder.reviewAuthorView.text = review.name
        holder.reviewContentView.text = review.review
//        holder.reviewRatingView.setImageResource(getRatingImageResource(review.rate))
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

//    private fun getRatingImageResource(rating: Int): Int {
//        return when (rating) {
//            1 -> R.drawable.ic_star_1
//            2 -> R.drawable.ic_star_2
//            3 -> R.drawable.ic_star_3
//            4 -> R.drawable.ic_star_4
//            5 -> R.drawable.ic_star_5
//            else -> R.drawable.ic_star
//        }
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewAuthorView: TextView = itemView.findViewById(R.id.review_author)
        val reviewContentView: TextView = itemView.findViewById(R.id.review_content)
        val reviewRatingView: RatingBar = itemView.findViewById(R.id.review_rating)
    }
}