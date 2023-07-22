package com.mohamednader.shoponthego.productinfo

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.example.Images
import com.mohamednader.shoponthego.databinding.FragmentCategoriesBinding.inflate


class ViewPagerProductinfoAdapter(photos: ArrayList<Images>) : PagerAdapter() {
    private val mPhotos: ArrayList<Images>

    init {
        mPhotos = photos
    }
private var images: ArrayList<Images> = mPhotos


    override fun getCount(): Int {
        return mPhotos.size
//        return images!!.size

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photo: Images = mPhotos[position]
        val inflater = LayoutInflater.from(container.context)
        val view: View = inflater.inflate(com.mohamednader.shoponthego.R.layout.productinfoitem, container, false)
        val imageView: ImageView = view.findViewById(com.mohamednader.shoponthego.R.id.image_viewproductinfo)
//        imageView.setImageResource(photo.src)
        Glide.with(imageView)
            .load(photo.src)
            .override(500, 500) // resize the image to 500x500 pixels
            .into(imageView)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Photo " + (position + 1)
    }
}