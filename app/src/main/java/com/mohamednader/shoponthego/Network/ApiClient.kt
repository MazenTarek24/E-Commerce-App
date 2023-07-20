package com.mohamednader.shoponthego.Network

import android.util.Log
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.ProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ApiClient : RemoteSource {


    private val TAG = "ApiClient_INFO_TAG"

    val apiService: ApiService by lazy {
        RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                val temp = ApiClient()
                instance = temp
                temp
            }
        }
    }

    override suspend fun getAllProducts(): Flow<List<Product>> {
        val response = apiService.getAllProducts()
        val productsList : Flow<List<Product>>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            productsList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            productsList = emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return productsList
    }

    override suspend fun getAllBrands(): Flow<List<SmartCollection>> {
        val response = apiService.getAllBrands()
        val brandList : Flow<List<SmartCollection>>
        Log.i(TAG, "getAllBrands: API-Client")
        if (response.isSuccessful)
        {
            brandList = flowOf(response.body()!!.smart_collections)
            Log.i(TAG, "getAllBrands: Done")

        }else{
            brandList = emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")

        }
        return brandList
    }

    override suspend fun getAllProductBrands(id: String): Flow<List<Product>> {
        val response = apiService.getAllBrandsProduct(id)
        val productList : Flow<List<Product>>
        Log.i(TAG, "getAllProducts API-Client")
        if (response.isSuccessful)
        {
            productList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllBrandsProducts: Done")

        }else{
            productList = emptyFlow()
            Log.i(TAG, "getAllBrandsProducts: ${response.errorBody().toString()}")
        }
        return productList
    }


}