package com.mohamednader.shoponthego

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator

class ProductInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)
        val springDotsIndicator = findViewById<SpringDotsIndicator>(R.id.dot2)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val adapter = ViewPagerProductinfoAdapter()
        viewPager.adapter = adapter
        springDotsIndicator.setViewPager(viewPager)
    }
}