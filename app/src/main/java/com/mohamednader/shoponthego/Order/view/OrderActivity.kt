package com.mohamednader.shoponthego.Order.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
import com.mohamednader.shoponthego.databinding.ActivityOrderBinding
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity() {

    lateinit var ordersAdapter: OrdersAdapter
    lateinit var orderLayoutManager: LayoutManager

    lateinit var binding: ActivityOrderBinding

    lateinit var viewModel: OrderViewModel
    lateinit var factory: OrderViewModelFactory

    private val TAG = "OrderActivity_INFO_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initRvOrder()


        factory = OrderViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this), ConcreteDataStoreSource(this)))
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)

        getAllOrder()


        binding.backArrowImg.setOnClickListener {
            OnBackPressed()
        }

    }

    private fun OnBackPressed() {
        val navController = Navigation.findNavController(binding.root)
        navController.popBackStack()
    }

    private fun getAllOrder() {
        lifecycleScope.launch {
            viewModel.orderList.collect { result ->
                when (result) {
                    is ApiState.Success<List<Order>> -> {
                        ordersAdapter.submitList(result.data)
                        Log.i(TAG, "fetch orders successfully: Loading...")

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
                viewModel.getAllOrders()
            }
        }
    }

    private fun initRvOrder() {
        ordersAdapter = OrdersAdapter()

        orderLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerOrder.apply {
            adapter = ordersAdapter
            layoutManager = orderLayoutManager
        }

    }

}