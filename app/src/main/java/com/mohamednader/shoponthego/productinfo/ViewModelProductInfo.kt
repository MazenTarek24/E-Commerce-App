package com.mohamednader.shoponthego.productinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.SingleProduct
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ViewModelProductInfo  (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _product: MutableStateFlow<ApiState<SingleProduct>> =
        MutableStateFlow<ApiState<SingleProduct>>(ApiState.Loading)
    val product : StateFlow<ApiState<SingleProduct>>
        get() = _product






    fun getProductWithIdFromNetwork(id:String){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getProductWithIDFromNetwork: HomeViewModel")
            repo.getProductWithId(id)
                .catch { e -> _product.value = ApiState.Failure(e)  }
                .collect{
                        data -> _product.value = ApiState.Success(data)
                    println(data!!.vendor+"sssssssssssssssssss")
                }
        }
    }


}