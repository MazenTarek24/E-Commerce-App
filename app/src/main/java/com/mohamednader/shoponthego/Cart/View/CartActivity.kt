package com.mohamednader.shoponthego.Cart.View

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
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
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.LineItem
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Payment.View.PaymentActivity
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.CustomProgress
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.Utils.convertCurrencyFromEGPTo
import com.mohamednader.shoponthego.databinding.ActivityCartBinding
import com.mohamednader.shoponthego.productinfo.ProductInfo
import kotlinx.coroutines.cancel
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
    lateinit var draftOrder: DraftOrder
    lateinit var customerId: String
    private lateinit var customProgress: CustomProgress
    var continueToCheckOut = false
    var lineItemsList = mutableListOf<LineItem>()

    var currencyISO = "EGP"
    var currencyRate = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this@CartActivity),
                ConcreteDataStoreSource(this@CartActivity)))
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)
        //Layouts

        getCurrency()

        //Progress Bar
        customProgress = CustomProgress.getInstance()
        firebaseAuth = Firebase.auth

        binding.backArrowImg.setOnClickListener {
            onBackPressed()
        }

        binding.checkOutBtn.setOnClickListener {
            if (continueToCheckOut) {
                val intent: Intent = Intent(this@CartActivity, PaymentActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@CartActivity, "You don't have any items!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun getCurrency() {
        cartViewModel.getStringDS(Constants.currencyKey).asLiveData()
            .observe(this@CartActivity) { result ->
                currencyISO = result ?: ""

                cartViewModel.getStringDS(Constants.rateKey).asLiveData()
                    .observe(this@CartActivity) { result ->
                        currencyRate = result?.toDouble() ?: 1.0

                        cartAdapter =
                            CartAdapter(currencyRate, currencyISO, this@CartActivity, this, this)
                        cartLayoutManager =
                            LinearLayoutManager(this@CartActivity, RecyclerView.VERTICAL, false)
                        binding.cartRecyclerView.apply {
                            adapter = cartAdapter
                            layoutManager = cartLayoutManager
                        }
                        apiRequests()
                    }
            }
    }


    private fun apiRequests() {
        lifecycleScope.launchWhenStarted {

            launch {
                cartViewModel.getStringDS(Constants.customerIdKey).asLiveData()
                    .observe(this@CartActivity) { customerID ->
                        // Update UI with the retrieved name
                        Log.i(TAG, "apiRequests: VIP: $customerID")
                        customerId = customerID!!
                        cartViewModel.getAllDraftOrdersFromNetwork(customerId.toLong())
                    }
            }


            launch {
                cartViewModel.draftOrdersList.collectLatest { result ->
                    when (result) {
                        is ApiState.Success<List<DraftOrder>> -> {
                            if (result.data.isNotEmpty()) {

                                cartAdapter.submitList(lineItemsList)
                                draftOrder = result.data.get(0)
                                lineItemsList = draftOrder.lineItems!!
                                Log.i(TAG,
                                        "draftOrdersList: Success: Draft Orders:  ${draftOrder.id}")
                                Log.i(TAG,
                                        "draftOrdersList: Success: Draft Orders:  ${draftOrder.email}")
                                Log.i(TAG, "draftOrdersList: Success: Draft Orders:  ${
                                    draftOrder.lineItems?.get(0)?.quantity
                                }")
                                cartAdapter.submitList(lineItemsList)
                                var total_items = 0
                                for (item in draftOrder.lineItems!!) {
                                    total_items += item.quantity!!
                                }
                                binding.totalItemsNumber.text = "Total (${total_items} item):"

                                binding.totalItemsPrice.text = "${
                                    convertCurrencyFromEGPTo((draftOrder.subtotalPrice)!!.toDouble(),
                                            currencyRate)
                                } $currencyISO"

                                showViews()
                                customProgress.hideProgress()
                                continueToCheckOut = true
                            } else {
                                Log.i(TAG, "onCreate: Success...The list is empty ${customerId}}")
                                hideViews()
                                continueToCheckOut = false
                                customProgress.hideProgress()
                            }
                            cancel()
                        }
                        is ApiState.Loading -> {
                            customProgress.showDialog(this@CartActivity, false)
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
                cartViewModel.updatedDraftCartOrder.collectLatest { result ->
                    when (result) {
                        is ApiState.Success<DraftOrder> -> {
                            cartAdapter.submitList(lineItemsList)
                            draftOrder = result.data
                            lineItemsList = result.data.lineItems!!
                            Log.i(TAG,
                                    "updatedDraftCartOrder: Success: Draft Orders:  ${draftOrder.id}")
                            Log.i(TAG,
                                    "updatedDraftCartOrder: Success: Draft Orders:  ${draftOrder.email}")
                            Log.i(TAG, "updatedDraftCartOrder: Success: Draft Orders:  ${
                                draftOrder.lineItems?.get(0)?.quantity
                            }")
                            try {
                                Log.i(TAG, "updatedDraftCartOrder: Success: Draft Orders:  ${
                                    draftOrder.lineItems?.get(1)?.quantity
                                }")
                            } catch (ex: Exception) {

                            }
                            Log.i(TAG, "updatedDraftCartOrder: Success: Draft Orders:  ${
                                draftOrder.lineItems?.size
                            }")

                            cartAdapter.submitList(lineItemsList)

                            var total_items = 0
                            for (item in draftOrder.lineItems!!) {
                                total_items += item.quantity!!
                            }
                            binding.totalItemsNumber.text = "Total (${total_items} item):"

                            binding.totalItemsPrice.text = "${
                                convertCurrencyFromEGPTo((draftOrder.subtotalPrice)!!.toDouble(),
                                        currencyRate)
                            } $currencyISO"

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
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun onPlusClickListener(id: Long) {
        var lineItemToUpdate = draftOrder.lineItems!!.find { it.id == id }
        if (lineItemToUpdate != null) {
            if (lineItemToUpdate.quantity!! < lineItemToUpdate.properties?.get(1)?.value?.toInt()!!) {
                updateQuantity(id, true)
            } else {
                Toast.makeText(this@CartActivity,
                        "Available in Stock (${lineItemToUpdate.properties!!.get(1).value.toInt()})",
                        Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onMinusClickListener(id: Long) {
        var lineItemToUpdate = draftOrder.lineItems!!.find { it.id == id }
        if (lineItemToUpdate != null) {
            if (lineItemToUpdate.quantity!! > 1) {
                updateQuantity(id, false)
            } else {
                Toast.makeText(this@CartActivity,
                        "Cannot order less than one item",
                        Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onDeleteClickListener(id: Long) {

        AlertDialog.Builder(this@CartActivity).setMessage("Do you want to delete this item")
            .setCancelable(false).setPositiveButton("Yes, Delete it") { dialog, _ ->


                val itemToRemove = draftOrder.lineItems!!.find { lineItem ->
                    lineItem.id == id
                }
                val position = lineItemsList.indexOf(itemToRemove)

                try{
                    Log.i(TAG, "onDeleteClickListener: ${position}")
                    cartAdapter.deleteItem(position)
                    lineItemsList.remove(itemToRemove)
                }catch (ex : Exception){
                    Toast.makeText(this@CartActivity, "Something Wrong! ", Toast.LENGTH_SHORT).show()
                    recreate()
                }


                Log.i(TAG, "onDeleteClickListener: AFTER UPDATE")

                if (position == 0) {
                    cartViewModel.deleteDraftOrderByID(draftOrder.id!!)
                    binding.emptyMsg.visibility = View.VISIBLE
                    binding.totalPriceContainer.visibility = View.GONE
                } else {
                    draftOrder = draftOrder.copy(lineItems = lineItemsList)
                    cartViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                            SingleDraftOrderResponse(draftOrder))
                }

                

                Toast.makeText(
                        this@CartActivity, "item Deleted!", Toast.LENGTH_SHORT
                ).show()
            }.setNegativeButton("No, Keep it", null).show()

    }

    private fun updateQuantity(id: Long, increment: Boolean) {
        val updatedLineItems = draftOrder.lineItems!!.map { lineItem ->
            if (lineItem.id == id) {
                val newQuantity =
                    if (increment) (lineItem.quantity ?: 0) + 1 else (lineItem.quantity ?: 0) - 1
                lineItem.copy(quantity = newQuantity)
            } else {
                lineItem
            }
        }

        Log.i(TAG, "updateQuantity: ${draftOrder.lineItems.toString()}")

        draftOrder = draftOrder.copy(lineItems = updatedLineItems.toMutableList())
        cartViewModel.updateDraftOrderCartOnNetwork(draftOrder.id!!,
                SingleDraftOrderResponse(draftOrder))
    }

}