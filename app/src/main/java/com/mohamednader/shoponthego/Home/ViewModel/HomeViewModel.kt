package com.mohamednader.shoponthego.Home.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _productsList: MutableStateFlow<ApiState<List<Product>>> =
        MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val productList : StateFlow<ApiState<List<Product>>>
        get() = _productsList


    private var _discountCodesList: MutableStateFlow<ApiState<List<DiscountCodes>>> =
        MutableStateFlow<ApiState<List<DiscountCodes>>>(ApiState.Loading)
    val discountCodesList : StateFlow<ApiState<List<DiscountCodes>>>
        get() = _discountCodesList

    private var _priceRulesList: MutableStateFlow<ApiState<List<PriceRules>>> =
        MutableStateFlow<ApiState<List<PriceRules>>>(ApiState.Loading)
    val priceRulesList : StateFlow<ApiState<List<PriceRules>>>
        get() = _priceRulesList



    fun getAllProductsFromNetwork(){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.getAllProducts()
                .catch { e -> _productsList.value = ApiState.Failure(e) }
                .collect{ data -> _productsList.value = ApiState.Success(data)
                }
        }
    }


    fun getDiscountCodesByPriceRuleIDFromNetwork(priceRuleId: Long){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getDiscountCodesByPriceRuleIDFromNetwork: HomeViewModel")
            repo.getDiscountCodesByPriceRuleID(priceRuleId)
                .catch { e -> _discountCodesList.value = ApiState.Failure(e) }
                .collect{ data -> _discountCodesList.value = ApiState.Success(data)
                }
        }
    }

    fun getAllPriceRulesFromNetwork(){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getAllPriceRulesFromNetwork: HomeViewModel")
            repo.getAllPriceRules()
                .catch { e -> _priceRulesList.value = ApiState.Failure(e) }
                .collect{ data -> _priceRulesList.value = ApiState.Success(data)
                }
        }
    }

}