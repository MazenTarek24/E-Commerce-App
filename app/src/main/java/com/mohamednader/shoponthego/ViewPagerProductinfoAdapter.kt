package com.mohamednader.shoponthego

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.mohamednader.shoponthego.databinding.FragmentCategoriesBinding.inflate


//class ViewPagerProductinfoAdapter(photos: List<Photo>) : PagerAdapter() {
    class ViewPagerProductinfoAdapter() : PagerAdapter() {
//    private val mPhotos: List<Photo>

//    init {
//        mPhotos = photos
//    }
private var images: Array<Int> =
    arrayOf<Int>(R.drawable.ic_dialog_dialer, R.drawable.ic_dialog_email, R.drawable.ic_dialog_dialer, R.drawable.ic_dialog_email, R.drawable.ic_btn_speak_now)


    override fun getCount(): Int {
//        return mPhotos.size
        return images!!.size

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val photo: Photo = mPhotos[position]
        val inflater = LayoutInflater.from(container.context)
        val view: View = inflater.inflate(com.mohamednader.shoponthego.R.layout.productinfoitem, container, false)
        val imageView: ImageView = view.findViewById(com.mohamednader.shoponthego.R.id.image_viewproductinfo)
//        imageView.setImageResource(photo.getResourceId())
        imageView.setImageResource(images[position])

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