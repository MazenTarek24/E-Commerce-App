package com.mohamednader.shoponthego.Network

import android.util.Log
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
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

    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        val response = apiService.getDiscountCodesByPriceRuleID(priceRuleId)
        val discountCodesList : Flow<List<DiscountCodes>>
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
        val priceRulesList : Flow<List<PriceRules>>
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