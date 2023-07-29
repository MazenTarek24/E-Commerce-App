package com.mohamednader.shoponthego.Order.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Order.viewmodel.OrderViewModel
import com.mohamednader.shoponthego.Order.viewmodel.OrderViewModelFactory
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.CustomProgress
import com.mohamednader.shoponthego.databinding.ActivityOrderBinding
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity() {

    lateinit var ordersAdapter: OrdersAdapter
    lateinit var orderLayoutManager: LayoutManager

    lateinit var binding: ActivityOrderBinding

    lateinit var viewModel: OrderViewModel
    lateinit var factory: OrderViewModelFactory
    lateinit var customerId: String
    var currencyISO = "EGP"
    var currencyRate = 1.0
    private lateinit var customProgress: CustomProgress

    private val TAG = "OrderActivity_INFO_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        customProgress = CustomProgress.getInstance()

        customProgress.showDialog(this@OrderActivity, false)

        factory = OrderViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this), ConcreteDataStoreSource(this)))
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)

        viewModel.getStringDS(Constants.currencyKey).asLiveData()
            .observe(this@OrderActivity)  { result ->
                currencyISO = result ?: ""

                viewModel.getStringDS(Constants.rateKey).asLiveData()
                    .observe(this@OrderActivity)  { result ->
                        currencyRate = result?.toDouble() ?: 1.0

                        ordersAdapter = OrdersAdapter(currencyRate, currencyISO)
                        initRvOrder()
                    }
            }






        binding.backArrowImg.setOnClickListener {
            onBackPressed()
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getAllOrder() {

        lifecycleScope.launchWhenStarted {



            launch {
                viewModel.getStringDS(Constants.customerIdKey).asLiveData()
                    .observe(this@OrderActivity) { customerID ->
                        // Update UI with the retrieved name
                        Log.i(TAG, "apiRequests: VIP: $customerID")
                        customerId = customerID!!
                        viewModel.getAllOrders(customerId.toLong())
                     }
            }



            viewModel.orderList.collect { result ->
                when (result) {
                    is ApiState.Success<List<Order>> -> {
                        ordersAdapter.submitList(result.data)
                        Log.i(TAG, "fetch orders successfully: Loading...")
                        customProgress.hideProgress()
                    }

                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading...")
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(this@OrderActivity,
                                "There Was An Error",
                                Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun initRvOrder() {


        orderLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerOrder.apply {
            adapter = ordersAdapter
            layoutManager = orderLayoutManager
        }

        getAllOrder()

    }

}