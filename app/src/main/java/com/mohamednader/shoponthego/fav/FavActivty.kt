package com.mohamednader.shoponthego.fav

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.example.DraftOrders
import com.example.example.SingleProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.DraftOrder
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityFavActivtyBinding
import com.mohamednader.shoponthego.databinding.ActivityProductInfoBinding
import com.mohamednader.shoponthego.productinfo.ViewModelProductInfo
import com.mohamednader.shoponthego.productinfo.ViewPagerProductinfoAdapter
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import kotlinx.coroutines.launch

class FavActivty : AppCompatActivity() {
    private val TAG = "fav_INFO_TAG"
    private var draftOrdersID: String? = null
    lateinit var recyclerAdapter: FavRecycleAdapter

    private lateinit var viewModelProductInfo: ViewModelFav
    private lateinit var factory: GenericViewModelFactory
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var myLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityFavActivtyBinding
    var listofproduct = mutableListOf<SingleProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        firebaseAuth = Firebase.auth
        recyclerAdapter = FavRecycleAdapter(this)
        initViews()
        viewModelProductInfo.getAllDraftsOrder()

        apicallForgetAllDrafts()
        apicallForgetdraftwithId()
        apicall()
        val sharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val draftID = sharedPreferences.getString("draftId", "defaultName")
        println(draftID+"ddddddddddddddddraft")
        binding.rcycyfav
            .apply {
            adapter = recyclerAdapter
            layoutManager = myLayoutManager
        }
    }

    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(), ConcreteLocalSource(this), ConcreteSharedPrefsSource(this)
            )
        )

        viewModelProductInfo =
            ViewModelProvider(this, factory).get(ViewModelFav::class.java)
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
                                println("IIIIIIIIIIIIIIddddddddddd"+draftOrder.id)
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
                            this@FavActivty, "There Was An Error", Toast.LENGTH_SHORT
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
                        recyclerAdapter.submitList(result.data.line_items)

                        val lineItem= result.data.line_items
                        println("heeeeeeeeeey"+result.data.line_items?.get(0)?.id)
                        val lineItems = result.data.line_items
                        val lineItemsIterator = result.data.line_items?.iterator()
                        while (lineItemsIterator!!.hasNext()) {
                            val lineItem = lineItemsIterator.next()
                            println("Line item id mmmmmmmmmmmm: ${lineItem.id}")
                            lineItem.product_id
                            viewModelProductInfo.getProductWithIdFromNetwork(lineItem.product_id.toString())

                        }



                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading... in 12:15")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(
                            this@FavActivty, "There Was An Error", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun apicall() {
        lifecycleScope.launch {

            viewModelProductInfo.product.collect { result ->
                when (result) {
                    is ApiState.Success<SingleProduct> -> {
                        Log.i(TAG, "onCreate: Success...product{${result.data.options.get(0)}")
              var list  = mutableListOf<SingleProduct>()
                        list.add(result.data)
                        listofproduct.add(result.data)


                    }
                    is ApiState.Loading -> {
//                                Log.i(TAG, "onCreate: Loading..."

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(
                            this@FavActivty, "There Was An Error", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }



}