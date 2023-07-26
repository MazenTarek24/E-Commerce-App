package com.mohamednader.shoponthego.Auth.Login.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.PostDraftOrder
import com.example.example.ResponseDraftOrderOb
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _draftorder: MutableStateFlow<ApiState<ResponseDraftOrderOb>> =
        MutableStateFlow<ApiState<ResponseDraftOrderOb>>(ApiState.Loading)
    val customerList : StateFlow<ApiState<ResponseDraftOrderOb>>
        get() = _draftorder


    private var _customer: MutableStateFlow<ApiState<List<Customer>>> =
        MutableStateFlow<ApiState<List<Customer>>>(ApiState.Loading)
    val customer : StateFlow<ApiState<List<Customer>>>
        get() = _customer



    fun createDraftOrder(draftOrder: PostDraftOrder){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.createDraftforfav(draftOrder)
                .catch { e -> _draftorder.value = ApiState.Failure(e) }
                .collect{ data -> _draftorder.value = ApiState.Success(data)
                }
        }
    }

    fun getAllCustomers(email:String){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.getAllCustomers()
                .catch { e -> _customer.value = ApiState.Failure(e) }
                .map { data -> data.filter { it.email == email } }
                .collect{ data -> _customer.value = ApiState.Success(data)
                }
        }
    }





}