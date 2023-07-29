package com.mohamednader.shoponthego.Auth.SignUp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SignUpViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _customer: MutableStateFlow<ApiState<Customer>> =
        MutableStateFlow<ApiState<Customer>>(ApiState.Loading)
    val customer: StateFlow<ApiState<Customer>>
        get() = _customer

    fun createCustomer(customer: SingleCustomerResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getProductWithIDFromNetwork: HomeViewModel")
            repo.createCustomer(customer).catch { e -> _customer.value = ApiState.Failure(e) }
                .collect { data ->
                    _customer.value = ApiState.Success(data)
                }
        }
    }

}