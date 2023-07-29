package com.mohamednader.shoponthego.fav

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItem
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityFavActivtyBinding
import kotlinx.coroutines.launch

class FavActivty : AppCompatActivity() {
    private val TAG = "fav_INFO_TAG"
    private var draftOrdersID: String? = null
    lateinit var recyclerAdapter: FavRecycleAdapter
    private val myLiveData = MutableLiveData<List<LineItem>>()
    private var listItems: MutableList<LineItem>? = mutableListOf<LineItem>()

    private lateinit var viewModelProductInfo: ViewModelFav
    private lateinit var factory: GenericViewModelFactory
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var myLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityFavActivtyBinding
    lateinit var draftOrder: DraftOrder
    var listofproduct = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        firebaseAuth = Firebase.auth
        recyclerAdapter = FavRecycleAdapter(this) {
            recyclerAdapter.submitList(listItems)

            listItems?.remove(it)
            recyclerAdapter.submitList(listItems)

            draftOrder = draftOrder.copy(lineItems = listItems)
            viewModelProductInfo.modifyDraftsOrder(SingleDraftOrderResponse(draftOrder),
                    draftOrdersID!!.toLong())

        }

        initViews()
        viewModelProductInfo.getAllDraftsOrder()

        apicallForgetAllDrafts()
        apicallForgetdraftwithId()
        apicall()

        binding.rcycyfav.apply {
            adapter = recyclerAdapter
            layoutManager = myLayoutManager
        }
        myLiveData.value = listItems?.toList()
        myLiveData.observe(this, Observer { lineItems ->

        })
    }

    private fun initViews() {

        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this),
                ConcreteDataStoreSource(this)))

        viewModelProductInfo = ViewModelProvider(this, factory).get(ViewModelFav::class.java)
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
                                viewModelProductInfo.getDraftOrderWithId(draftOrder.id!!)

                            }

                        }

                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading... in DDDDDDDDraft Orders")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@FavActivty, "There Was An Error", Toast.LENGTH_SHORT)
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
                        draftOrder = result.data
                        println("12:51" + result.data.id)
                        listItems = result.data.lineItems?.toMutableList()

                        println("lissssssssssstItttttem" + listItems)
//                        listItems?.removeAt(0)
                        recyclerAdapter.submitList(listItems)

                        val list = result.data.lineItems
                        listItems = result.data.lineItems?.toMutableList()
                        val lineItems = result.data.lineItems
                        val lineItemsIterator = result.data.lineItems?.iterator()
                        while (lineItemsIterator!!.hasNext()) {
                            val lineItem = lineItemsIterator.next()
                            lineItem.productId
                            viewModelProductInfo.getProductWithIdFromNetwork(lineItem.productId.toString())

                        }

                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading... in 12:15")

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@FavActivty, "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun apicall() {
        lifecycleScope.launch {

            viewModelProductInfo.product.collect { result ->
                when (result) {
                    is ApiState.Success<Product> -> {
                        Log.i(TAG, "onCreate: Success...product{${result.data.options?.get(0)}")
                        var list = mutableListOf<Product>()
                        list.add(result.data)
                        listofproduct.add(result.data)

                    }
                    is ApiState.Loading -> {
//                                Log.i(TAG, "onCreate: Loading..."

                    }
                    is ApiState.Failure -> {
                        //hideViews()

                        Toast.makeText(this@FavActivty, "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

}