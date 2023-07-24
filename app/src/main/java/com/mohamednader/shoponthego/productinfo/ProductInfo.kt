package com.mohamednader.shoponthego.productinfo

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.example.Image
import com.example.example.Images
import com.example.example.SingleProduct
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.ViewModel.HomeViewModel
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityProductInfoBinding
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.coroutines.launch

class ProductInfo : AppCompatActivity() {
    private val TAG = "ProductInfo_INFO_TAG"

    //View Model Members
    private lateinit var viewModelProductInfo: ViewModelProductInfo
    private lateinit var factory: GenericViewModelFactory
    var images: ArrayList<Images> ?=null

    private lateinit var binding: ActivityProductInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomNumber = (7..10).random()
        binding.ratingBar.rating=randomNumber.toFloat()
        binding.recyclerView.layoutManager =LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val reviewList = listOf(
            Review("John Doe", "This is a great product!", 5),
            Review("Jane Doe", "This product is okay.", 3),
            Review("Bob Smith", "I don't like this product.", 1)
        )
        val adapter = ReviewAdapter(reviewList)
       binding.recyclerView.adapter = adapter

        initViews()


        apicall()


        viewModelProductInfo.getProductWithIdFromNetwork("8443620360509")


    }

    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(this),
                ConcreteSharedPrefsSource(this)
            )
        )

        viewModelProductInfo =
            ViewModelProvider(this, factory).get(ViewModelProductInfo::class.java)
    }

    private fun apicall() {
        lifecycleScope.launch {

                viewModelProductInfo.product
                    .collect { result ->
                        when (result) {
                            is ApiState.Success<SingleProduct> -> {
                                Log.i(TAG, "onCreate: Success...{${result.data.options.get(0)}")
                                binding.descriptiontxv.text=result.data.bodyHtml
                                images=result.data.images
                               binding.pricetv.text= result.data.variants.get(0).price
                                binding.productnametv.text=result.data.title
                                println(result.data.options.get(0).values)
                                val springDotsIndicator = findViewById<SpringDotsIndicator>(R.id.dot2)
                                val viewPager = findViewById<ViewPager>(R.id.view_pager)
                                val adapter = ViewPagerProductinfoAdapter(result.data.images)
                                viewPager.adapter = adapter
                                springDotsIndicator.setViewPager(viewPager)


                            }
                            is ApiState.Loading -> {
//                                Log.i(TAG, "onCreate: Loading..."

                            }
                            is ApiState.Failure -> {
                                //hideViews()

                                Toast.makeText(
                                    this@ProductInfo, "There Was An Error", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
        }
    fun getRandomNumber(): Int {
        val random = java.util.Random()
        return random.nextInt(51) + 50
    }

    }