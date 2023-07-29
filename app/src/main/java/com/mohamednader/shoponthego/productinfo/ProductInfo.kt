package com.mohamednader.shoponthego.productinfo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.*
import com.mohamednader.shoponthego.Model.Pojo.Products.Image
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.Constants.COLORS_TYPE
import com.mohamednader.shoponthego.Utils.Constants.SIZES_TYPE
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityProductInfoBinding
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.coroutines.launch

class ProductInfo : AppCompatActivity() {
    private val TAG = "ProductInfo_INFO_TAG"
    private lateinit var firebaseAuth: FirebaseAuth
    private var product: Product = Product()
    lateinit var productObject: Product
    private var draftOrdersID: String = ""
    private var lineItems: List<LineItem>? = listOf<LineItem>()
    var mutable = lineItems?.toMutableList()

    //View Model Members
    private lateinit var viewModelProductInfo: ViewModelProductInfo
    private lateinit var colorsAdapter: ColorsAndSizesAdapter
    private lateinit var sizesAdapter: ColorsAndSizesAdapter
    private lateinit var factory: GenericViewModelFactory
    var images: List<Image> = emptyList()
    lateinit var draftOrder: DraftOrder
    lateinit var customerId: String
    var productId: Long = 0

    private lateinit var binding: ActivityProductInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addtocart.setOnClickListener {
            addItemToCart()
            Toast.makeText(this@ProductInfo, "adding to dav", Toast.LENGTH_SHORT).show()
        }
        firebaseAuth = Firebase.auth
        val intent = intent

        productId = intent.getLongExtra("id", 0)


        colorsAdapter = ColorsAndSizesAdapter(COLORS_TYPE)
        sizesAdapter = ColorsAndSizesAdapter(SIZES_TYPE)
        val randomNumber = (7..10).random()
        binding.ratingBar.rating = randomNumber.toFloat()
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val reviewList = listOf(Review("John Doe", "This is a great product!", 5),
                Review("Jane Doe", "This product is okay.", 3),
                Review("Bob Smith", "I don't like this product.", 1))
        val adapter = ReviewAdapter(reviewList)
        binding.recyclerView.adapter = adapter
        setupColorsRecyclerview()
        setupSizesRecyclerview()

        initViews()

        viewModelProductInfo.getAllDraftsOrder()

        apicall()

        binding.Addtofav.setOnClickListener {
            val mutablelist = lineItems?.toMutableList()
            mutablelist?.add(LineItem(productId,
                    product.variants?.get(0)?.id,
                    productId,
                    product.title,
                    "",
                    product.bodyHtml,
                    "",
                    1,
                    true,
                    true,
                    true,
                    "",
                    1,
                    AppliedDiscount("", "", "", "", ""),
                    product.title,
                    listOf(LineItemProperties("img_src", product.image?.src!!)),
                    true,
                    ""))



            if (draftOrdersID.isEmpty()) {
                val draftOrder = DraftOrder(lineItems = mutablelist, customer = Customer(customerId.toLong()), note = "favDraft")
                viewModelProductInfo.createDraftOrder(SingleDraftOrderResponse(draftOrder))
            } else {
                viewModelProductInfo.modifyDraftsOrder(SingleDraftOrderResponse(DraftOrder(lineItems = mutablelist,
                        note = "favDraft")), draftOrdersID.toLong())
                Toast.makeText(this@ProductInfo, "adding to Favourite", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun initViews() {

        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this),
                ConcreteDataStoreSource(this)))

        viewModelProductInfo =
            ViewModelProvider(this, factory).get(ViewModelProductInfo::class.java)
    }

    private fun apicall() {
        lifecycleScope.launch {

            launch {
                viewModelProductInfo.getStringDS(Constants.customerIdKey).asLiveData()
                    .observe(this@ProductInfo) { customerID ->
                        // Update UI with the retrieved name
                        Log.i(TAG, "apiRequests: VIP: $customerID")
                        customerId = customerID!!

                        viewModelProductInfo.getProductWithIdFromNetwork(productId.toString())
                        viewModelProductInfo.getProductByIdFromNetwork(productId)
                        apicallForgetdraftwithId()
                        apicallForgetAllDrafts()
                        apicallForModifyDrafts()

                    }
            }

            launch {
                viewModelProductInfo.product.collect { result ->
                    when (result) {
                        is ApiState.Success<Product> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.options?.get(0)}")
                            binding.descriptiontxv.text = result.data.bodyHtml
                            product = result.data
                            images = result.data.images!!
                            binding.pricetv.text = result.data.variants?.get(0)?.price +" LE"
                            binding.productnametv.text = result.data.title
                            println(result.data.options?.get(0)?.values)

                            if (result.data.options?.get(0)?.name == "Size") {
                                sizesAdapter.differ.submitList(result.data.options.get(0)?.values)
                            }
                            if (result.data.options?.get(0)?.name == "Color") {
                                colorsAdapter.differ.submitList(result.data.options.get(0)?.values)
                            }

                            val springDotsIndicator = findViewById<SpringDotsIndicator>(R.id.dot2)
                            val viewPager = findViewById<ViewPager>(R.id.view_pager)
                            val adapter = ViewPagerProductinfoAdapter(result.data.images)
                            viewPager.adapter = adapter
                            springDotsIndicator.setViewPager(viewPager)
                            binding.progressBar.visibility = View.GONE
                            binding.scrollView2.visibility = View.VISIBLE

                        }
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.scrollView2.visibility = View.GONE

                        }
                        is ApiState.Failure -> {
                            //hideViews()

                            Toast.makeText(this@ProductInfo,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            launch {
                viewModelProductInfo.productObject.collect { result ->
                    when (result) {
                        is ApiState.Success<Product> -> {
                            productObject = result.data

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@ProductInfo,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }



            launch {
                viewModelProductInfo.draftOrdersCartList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<DraftOrder>> -> {
                            if (result.data.isNotEmpty()) {
                                draftOrder = result.data.get(0)
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${draftOrder.id}")
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${draftOrder.email}")

                                val prop = listOf(LineItemProperties("img_src",
                                        productObject.image?.src!!),
                                        LineItemProperties("quantity",
                                                productObject.variants!!.get(0).inventoryQuantity.toString()))

                                draftOrder.lineItems!!.add(LineItem(variantId = productObject.variants!!.get(
                                        0).id, quantity = 1, properties = prop))


                                Log.i(TAG,
                                        "onCreate: Success: Draft Orders:  ${draftOrder.lineItems}")
                                viewModelProductInfo.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                                        SingleDraftOrderResponse(draftOrder))
                            } else {
                                Log.i(TAG,
                                        "onCreate: updatedDraftOrder Success...The list is empty")

                                val prop = listOf(LineItemProperties("img_src",
                                        productObject.image?.src!!),
                                        LineItemProperties("quantity",
                                                productObject.variants!!.get(0).inventoryQuantity.toString()))

                                val lineItem =
                                    LineItem(variantId = productObject.variants!!.get(0).id,
                                            quantity = 1,
                                            properties = prop)

                                val customer = Customer(id = customerId.toLong())

                                draftOrder = DraftOrder(lineItems = mutableListOf(lineItem),
                                        customer = customer,
                                        note = "cartDraft")

                                viewModelProductInfo.addDraftOrderCartOnNetwork(
                                        SingleDraftOrderResponse(draftOrder))
                            }
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@ProductInfo,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            launch {
                viewModelProductInfo.updatedDraftCartOrder.collect { result ->
                    when (result) {
                        is ApiState.Success<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder> -> {
                            val draftOrder = result.data
                            Log.i(TAG,
                                    "onCreate: updatedDraftOrder Success: Draft Orders Updated:  ${draftOrder.id}")
                            Log.i(TAG,
                                    "onCreate:updatedDraftOrder Success: Draft Orders Updated:  ${draftOrder.email}")
                            Log.i(TAG,
                                    "onCreate: Success: updatedDraftOrder Draft Orders Updated:  ${draftOrder.lineItems}")
//                            Toast.makeText(this@ProductInfo, "Added to Cart", Toast.LENGTH_SHORT)
//                                .show()
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@ProductInfo,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


            launch {
                viewModelProductInfo.newDraftOrder.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {
                            val draftOrder = result.data
                            Log.i(TAG,
                                    "onCreate: newDraftOrder Success: Draft Orders Updated:  ${draftOrder.id}")
                            Log.i(TAG,
                                    "onCreate:newDraftOrder Success: Draft Orders Updated:  ${draftOrder.email}")
                            Log.i(TAG,
                                    "onCreate: Success: newDraftOrder Draft Orders Updated:  ${draftOrder.lineItems}")
                            Toast.makeText(this@ProductInfo, "Added to Cart", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: newDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@ProductInfo,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }

    private fun apicallForgetAllDrafts() {
        lifecycleScope.launch {

            viewModelProductInfo.drafts.collect { result ->
                when (result) {
                    is ApiState.Success<List<DraftOrder>> -> {
                        for (draftOrder in result.data) {
                            val currentUser = firebaseAuth.currentUser
                            if (currentUser?.email == draftOrder.customer?.email && draftOrder.note == "favDraft") {
                                draftOrdersID = draftOrder.id.toString()
                                println(draftOrder.id)
                                viewModelProductInfo.getDraftOrderWithId(draftOrder.id!!)

                            }

                        }

                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading... in DDDDDDDDraft Orders")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@ProductInfo, "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
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
                    is ApiState.Success<DraftOrder> -> {
                        println("AAAAAAAAAAAAAAa" + result.data.id)

                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading... in DDDDDDDDraft Orders")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@ProductInfo, "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
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
                        result.data.lineItems
                        lineItems = result.data.lineItems

                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading... in 12:15")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@ProductInfo, "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun addItemToCart() {
        Log.i(TAG, "addItemToCart: CUSTOMER ID ${customerId.toLong()}")
        viewModelProductInfo.getAllDraftOrdersCartFromNetwork(customerId.toLong())
    }

}
