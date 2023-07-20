package com.mohamednader.shoponthego.Home.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
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

    private var _brandList : MutableStateFlow<ApiState<List<SmartCollection>>> =
        MutableStateFlow(ApiState.Loading)

    val brandList : StateFlow<ApiState<List<SmartCollection>>> get() = _brandList

    fun getAllProductsFromNetwork(){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getAllProductsFromNetwork: ")
            repo.getAllProducts()
                .catch { e -> _productsList.value = ApiState.Failure(e) }
                .collect{ data -> _productsList.value = ApiState.Success(data)
                }
        }
    }

    fun getAllBrands()
    {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG , "fetchBrandsFromApi")
            repo.getAllBrands().catch { error->
                _brandList.value = ApiState.Failure(error)
            }.collect { brand ->
                _brandList.value = ApiState.Success(brand)
            }
        }
    }



}