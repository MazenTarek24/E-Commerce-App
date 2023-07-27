package com.mohamednader.shoponthego.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.ViewModel.HomeViewModel
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.productinfo.ProductInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private val TAG = "SERACH_INFO_TAG"
    private lateinit var adapter: MyListAdapter
    private val itemList = mutableListOf<Product>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var factory: GenericViewModelFactory
    val sharedFlow = MutableSharedFlow<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()
        recyclerView = findViewById(R.id.rv_names)
        textView = findViewById(R.id.et_name)

        textView.addTextChangedListener(textWatcher)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter = MyListAdapter(this) {
            val intent = Intent(this, ProductInfo::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        homeViewModel.getAllProductsFromNetwork()
        lifecycleScope.launch {
            sharedFlow
                .debounce(300)
                .map { st ->
                    itemList.filter { item -> item.title?.startsWith(st, true)!! }
                }.collect {
                    adapter.submitList(it)
                }
        }
        apiRequests()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch {
                if (s.toString() == "") {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE

                    sharedFlow.emit(s.toString())
                }
            }
        }

    }

    private fun initViews() {

        factory = GenericViewModelFactory(
                Repository.getInstance(
                        ApiClient.getInstance(),
                        ConcreteLocalSource(this),
                        ConcreteDataStoreSource(this)
                )
        )

        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }

    private fun apiRequests() {
        lifecycleScope.launchWhenStarted {

            launch {

                homeViewModel.productList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<Product>> -> {
                            Log.i(TAG,
                                    "onCreate: Success.ssssssssssssssssss..{${result.data.get(0).title}}")
                            result.data.forEach { product ->
                                itemList.add(product)

                            }
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(
                                    this@SearchActivity,
                                    "There Was An Error",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}