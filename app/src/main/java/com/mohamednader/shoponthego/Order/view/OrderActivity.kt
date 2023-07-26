package com.mohamednader.shoponthego.Order.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Model.order.OrderX
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Order.view.viewmodel.OrderViewModel
import com.mohamednader.shoponthego.Order.view.viewmodel.OrderViewModelFactory

import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.databinding.ActivityOrderBinding
import com.mohamednader.shoponthego.orderdetails.view.OrderDetailsActivity
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity()  {

    lateinit var ordersAdapter: OrdersAdapter
    lateinit var orderLayoutManager : LayoutManager

    lateinit var binding : ActivityOrderBinding

    lateinit var viewModel: OrderViewModel
    lateinit var factory : OrderViewModelFactory

    private val TAG = "OrderActivity_INFO_TAG"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ordersAdapter = OrdersAdapter()

        orderLayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.recyclerOrder.apply {
            adapter = ordersAdapter
            layoutManager = orderLayoutManager
        }


        factory = OrderViewModelFactory(Repository.getInstance(ApiClient.getInstance() ,
            ConcreteLocalSource(this) ,ConcreteSharedPrefsSource(this)))
        viewModel = ViewModelProvider(this,factory).get(OrderViewModel::class.java)

        getAllOrder()


    }

    private fun getAllOrder()
    {
        lifecycleScope.launch {
            viewModel.orderList.collect{result->
                when(result)
                {
                    is ApiState.Success<List<OrderX>> -> {
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



}