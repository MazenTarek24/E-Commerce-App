package com.mohamednader.shoponthego.productinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.example.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.AppliedDiscount
import com.mohamednader.shoponthego.Model.Pojo.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.LineItems
import com.mohamednader.shoponthego.Model.Pojo.TaxLine
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.Constants.COLORS_TYPE
import com.mohamednader.shoponthego.Utils.Constants.SIZES_TYPE
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityProductInfoBinding
import com.mohamednader.shoponthego.fav.FavActivty
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.coroutines.launch


class ProductInfo : AppCompatActivity() {
    private val TAG = "ProductInfo_INFO_TAG"
    private lateinit var firebaseAuth: FirebaseAuth
    private var product: SingleProduct = SingleProduct()
    private var draftOrdersID: String = ""
    private var lineItems: List<com.mohamednader.shoponthego.Model.Pojo.LineItems>? =
        listOf<com.mohamednader.shoponthego.Model.Pojo.LineItems>()
    var mutable =lineItems?.toMutableList()

    //View Model Members
    private lateinit var viewModelProductInfo: ViewModelProductInfo
    private lateinit var colorsAdapter: ColorsAndSizesAdapter
    private lateinit var sizesAdapter: ColorsAndSizesAdapter
    private lateinit var factory: GenericViewModelFactory
    var images: ArrayList<Images>? = null

    private lateinit var binding: ActivityProductInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addtocart.setOnClickListener { val intent = Intent(this, FavActivty::class.java)
            startActivity(intent) }
        firebaseAuth = Firebase.auth
        val intent = intent
        val productId = intent.getLongExtra("id", 0)

        colorsAdapter = ColorsAndSizesAdapter(COLORS_TYPE)
        sizesAdapter = ColorsAndSizesAdapter(SIZES_TYPE)
        val randomNumber = (7..10).random()
        binding.ratingBar.rating = randomNumber.toFloat()
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val reviewList = listOf(
            Review("John Doe", "This is a great product!", 5),
            Review("Jane Doe", "This product is okay.", 3),
            Review("Bob Smith", "I don't like this product.", 1)
        )
        val adapter = ReviewAdapter(reviewList)
        binding.recyclerView.adapter = adapter
        setupColorsRecyclerview()
        setupSizesRecyclerview()

        initViews()

        viewModelProductInfo.getAllDraftsOrder()

        apicall()
        apicallForgetdraftwithId()
        apicallForgetAllDrafts()
        apicallForModifyDrafts()

        binding.Addtofav.setOnClickListener {
            val mutablelist =lineItems?.toMutableList()
            mutablelist?.add(LineItems(
                productId,
                product.variants.get(0).id,
                productId,
                product.title,
                "",
                product.bodyHtml,
                "",
                1,
                true,
                true,
                true,
                product.images.get(1).src,
                1,
                listOf(TaxLine("", "")),
                AppliedDiscount("", "", "", "", ""),
                product.title,
                null,
                true,
                "",
                ""
            ))

            viewModelProductInfo.modifyDraftsOrder(
                DraftOrderResponse(
                    DraftOrder(
                        productId, "", "", true, "", "", "", "", true, "", "", "",mutablelist


                    )
                ), draftOrdersID.toLong()
            )

        }

        viewModelProductInfo.getProductWithIdFromNetwork(productId.toString())

    }

    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(), ConcreteLocalSource(this), ConcreteSharedPrefsSource(this)
            )
        )

        viewModelProductInfo =
            ViewModelProvider(this, factory).get(ViewModelProductInfo::class.java)
    }

    private fun apicall() {
        lifecycleScope.launch {

            viewModelProductInfo.product.collect { result ->
                    when (result) {
                        is ApiState.Success<SingleProduct> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.options.get(0)}")
                            binding.descriptiontxv.text = result.data.bodyHtml
                            product = result.data
                            images = result.data.images
                            binding.pricetv.text = result.data.variants.get(0).price
                            binding.productnametv.text = result.data.title
                            println(result.data.options.get(0).values)

                            if (result.data.options.get(0).name == "Size") {
                                sizesAdapter.differ.submitList(result.data.options.get(0).values)
                            }
                            if (result.data.options.get(0).name == "Color") {
                                colorsAdapter.differ.submitList(result.data.options.get(0).values)
                            }


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

    private fun apicallForgetAllDrafts() {
        lifecycleScope.launch {

            viewModelProductInfo.drafts.collect { result ->
                    when (result) {
                        is ApiState.Success<List<DraftOrders>> -> {
                            for (draftOrder in result.data) {
                                val currentUser = firebaseAuth.currentUser
                                if (currentUser?.email == draftOrder.customer?.email) {
                                    draftOrdersID = draftOrder.id.toString()
                                    println(draftOrder.id)
                                    viewModelProductInfo.getDraftOrderWithId(draftOrder.id)

                                }

                            }

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading... in DDDDDDDDraft Orders")

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

    private fun setupSizesRecyclerview() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizantalSpacingItemDecorator(45))
        }
    }

    private fun setupColorsRecyclerview() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizantalSpacingItemDecorator(45))
        }
    }

    private fun apicallForModifyDrafts() {
        lifecycleScope.launch {

            viewModelProductInfo.modifydraft.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrdermo> -> {

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading... in DDDDDDDDraft Orders")

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

    private fun apicallForgetdraftwithId() {
        lifecycleScope.launch {

            viewModelProductInfo.draftwithid.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {
                            println("12:51" + result.data.id)
                            result.data.line_items
                            lineItems = result.data.line_items


                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading... in 12:15")

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

}
