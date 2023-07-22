package com.mohamednader.shoponthego.Network

import android.util.Log
import com.example.example.Customerre
import com.example.example.PostCustomer
import com.example.example.ResponseCustomer
import com.example.example.SingleProduct
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Model.Pojo.customer.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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
        val productsList: Flow<List<Product>>
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
        val brandList: Flow<List<SmartCollection>>
        Log.i(TAG, "getAllBrands: API-Client")
        if (response.isSuccessful) {
            brandList = flowOf(response.body()!!.smart_collections)
            Log.i(TAG, "getAllBrands: Done")

        } else {
            brandList = emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")

        }
        return brandList
    }

    override suspend fun getAllProductBrands(id: String): Flow<List<Product>> {
        val response = apiService.getAllBrandsProduct(id)
        val productList: Flow<List<Product>>
        Log.i(TAG, "getAllProducts API-Client")
        if (response.isSuccessful) {
            productList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllBrandsProducts: Done")

        } else {
            productList = emptyFlow()
            Log.i(TAG, "getAllBrandsProducts: ${response.errorBody().toString()}")
        }
        return productList
    }

    override suspend fun getProductWithId(id: String): Flow<SingleProduct> {
        val response = apiService.getProductWithId(id)
        val product: Flow<SingleProduct>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            product = flowOf(response.body()!!.product)
            Log.i(TAG, "getAllProducts: Done")
        } else {
        return emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return product    }

    override suspend fun createCustomer(customer: PostCustomer): Flow<Customerre> {
        val response = apiService.createCustomer(customer)
        val product: Flow<Customerre>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            product = flowOf(response.body()!!.customer)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            return emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return product     }

    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        val response = apiService.getDiscountCodesByPriceRuleID(priceRuleId)
        val discountCodesList: Flow<List<DiscountCodes>>
        Log.i(TAG, "getDiscountCodesByPriceRuleID: API-Client")
        if (response.isSuccessful) {
            discountCodesList = flowOf(response.body()!!.discountCodesList)
            Log.i(TAG, "getDiscountCodesByPriceRuleID: Done")
        } else {
            discountCodesList = emptyFlow()
            Log.i(TAG, "getDiscountCodesByPriceRuleID: ${response.errorBody().toString()}")
        }
        return discountCodesList
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRules>> {
        val response = apiService.getAllPriceRules()
        val priceRulesList: Flow<List<PriceRules>>
        Log.i(TAG, "getAllPriceRules: API-Client")
        if (response.isSuccessful) {
            priceRulesList = flowOf(response.body()!!.priceRulesList)
            Log.i(TAG, "getAllPriceRules: Done")
        } else {
            priceRulesList = emptyFlow()
            Log.i(TAG, "getAllPriceRules: ${response.errorBody().toString()}")
        }
        return priceRulesList
    }




}