package com.mohamednader.shoponthego.BranProduct.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BrandProductViewModel(val repository: Repository) : ViewModel() {

    private val TAG = "BrandProductViewModel_INFO_TAG"

    private var  _productList : MutableStateFlow<ApiState<List<Product>>> =
        MutableStateFlow(ApiState.Loading)
    val productList : StateFlow<ApiState<List<Product>>>
        get() = _productList

    fun getAllBrandProduct(id : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllBrandProducts: ")
            repository.getAllProductBrands(id)
                .catch { error->
                    _productList.value = ApiState.Failure(error)
                }.collect{ products->
                    _productList.value = ApiState.Success(products)
                }
        }
    }


}