package com.mohamednader.shoponthego.Cart.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohamednader.shoponthego.Cart.View.Adapters.CartAdapter
import com.mohamednader.shoponthego.Cart.View.Adapters.OnPlusMinusClickListener
import com.mohamednader.shoponthego.Cart.View.Adapters.OnProductClickListener
import com.mohamednader.shoponthego.Cart.ViewModel.CartViewModel
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityCartBinding
import com.mohamednader.shoponthego.productinfo.ProductInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity(), OnProductClickListener, OnPlusMinusClickListener {
    val TAG = "CartActivity_INFO_TAG"
    private lateinit var binding: ActivityCartBinding

    //View Model Members
    private lateinit var cartViewModel: CartViewModel
    private lateinit var factory: GenericViewModelFactory

    //Recycler View
    lateinit var cartAdapter: CartAdapter
    lateinit var cartLayoutManager: LinearLayoutManager
    private lateinit var firebaseAuth: FirebaseAuth

    //Needed Variables
    val EXTRA_PRODUCT_ID = "productID"
    lateinit var draftOrder: DraftOrder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this@CartActivity),
                ConcreteSharedPrefsSource(this@CartActivity)))
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)
        //Layouts
        cartAdapter = CartAdapter(this@CartActivity, this, this)
        cartLayoutManager = LinearLayoutManager(this@CartActivity, RecyclerView.VERTICAL, false)

        binding.cartRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = cartLayoutManager
        }
        firebaseAuth = Firebase.auth

        binding.backArrowImg.setOnClickListener{
            onBackPressed()
        }

        apiRequests()
    }

    private fun apiRequests() {
        lifecycleScope.launchWhenStarted {
            launch {
                cartViewModel.draftOrdersList.collectLatest { result ->
                    when (result) {
                        is ApiState.Success<List<DraftOrder>> -> {
                            if (result.data.isNotEmpty()) {
                                draftOrder = result.data.get(0)
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${draftOrder.id}")
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${draftOrder.email}")
                                Log.i(TAG, "onCreate: Success: Draft Orders:  ${
                                    draftOrder.lineItems?.get(0)?.quantity
                                }")
                                cartAdapter.submitList(draftOrder.lineItems)
                                var total_items = 0
                                for (item in draftOrder.lineItems!!) {
                                    total_items += item.quantity!!
                                }
                                binding.totalItemsNumber.text = "Total (${total_items} item):"
                                binding.totalItemsPrice.text = "${draftOrder.subtotalPrice} EGP"
                                showViews()
                            } else {
                                Log.i(TAG, "onCreate: Success...The list is empty}")
                                hideViews()
                            }
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(this@CartActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }



            launch {
                cartViewModel.updatedDraftCartOrder.collect { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {
                            draftOrder = result.data
                            Log.i(TAG,
                                    "onCreate: updatedDraftOrder Success: Draft Orders Updated:  ${draftOrder.id}")
                            Log.i(TAG,
                                    "onCreate:updatedDraftOrder Success: Draft Orders Updated:  ${draftOrder.email}")
                            Log.i(TAG,
                                    "onCreate: Success: updatedDraftOrder Draft Orders Updated:  ${draftOrder.lineItems}")
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: updatedDraftOrder Loading...")
                        }
                        is ApiState.Failure -> { //hideViews()
                            Toast.makeText(this@CartActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        cartViewModel.getAllDraftOrdersFromNetwork(Constants.customerID)
    }

    private fun showViews() {
        binding.totalItemsNumber.visibility = View.VISIBLE
        binding.totalItemsPrice.visibility = View.VISIBLE
        binding.emptyMsg.visibility = View.GONE
    }

    private fun hideViews() {
        binding.totalItemsNumber.visibility = View.GONE
        binding.totalItemsPrice.visibility = View.GONE
        binding.emptyMsg.visibility = View.VISIBLE
    }

    override fun onProductClickListener(id: Long) {
        val intent: Intent = Intent(this@CartActivity, ProductInfo::class.java)
        intent.putExtra(EXTRA_PRODUCT_ID, id)
        startActivity(intent)
    }

    override fun onPlusClickListener(productVariantId: Long) {
        var lineItemToUpdate = draftOrder.lineItems!!.find { it.variantId == productVariantId }
        if (lineItemToUpdate != null) {
            if (lineItemToUpdate.quantity!! < lineItemToUpdate.properties.get(1).value.toInt()) {
                updateQuantity(productVariantId, true)
                cartViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                        SingleDraftOrderResponse(draftOrder))
            } else {
                Toast.makeText(this@CartActivity,
                        "Available in Stock (${lineItemToUpdate.properties.get(1).value.toInt()})",
                        Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onMinusClickListener(productVariantId: Long) {
        var lineItemToUpdate = draftOrder.lineItems!!.find { it.variantId == productVariantId }
        if (lineItemToUpdate != null) {
            if (lineItemToUpdate.quantity!! > 1) {
                updateQuantity(productVariantId, false)
                cartViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                        SingleDraftOrderResponse(draftOrder))
            } else {
                Toast.makeText(this@CartActivity,
                        "Cannot order less than one item",
                        Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun updateQuantity(productVariantId: Long, increment: Boolean) {
        val updatedLineItems = draftOrder.lineItems!!.map { lineItem ->
            if (lineItem.variantId == productVariantId) {
                val newQuantity =
                    if (increment) (lineItem.quantity ?: 0) + 1 else (lineItem.quantity ?: 0) - 1
                lineItem.copy(quantity = newQuantity)
            } else {
                lineItem
            }
        }
        draftOrder = draftOrder.copy(lineItems = updatedLineItems.toMutableList())
        cartAdapter.submitList(updatedLineItems)

        var total_items = 0
        for (item in draftOrder.lineItems!!) {
            total_items += item.quantity!!
        }
        binding.totalItemsNumber.text = "Total (${total_items} item):"
        binding.totalItemsPrice.text = "${draftOrder.subtotalPrice} EGP"
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}