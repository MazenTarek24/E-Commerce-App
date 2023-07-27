package com.mohamednader.shoponthego.Auth.Login.ViewModel

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _draftorder: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val customerList: StateFlow<ApiState<DraftOrder>>
        get() = _draftorder

    private var _customer: MutableStateFlow<ApiState<List<Customer>>> =
        MutableStateFlow<ApiState<List<Customer>>>(ApiState.Loading)
    val customer: StateFlow<ApiState<List<Customer>>>
        get() = _customer

    fun createDraftOrder(draftOrder: SingleDraftOrderResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.addDraftOrder(draftOrder)
                .catch { e -> _draftorder.value = ApiState.Failure(e) }.collect { data ->
                    _draftorder.value = ApiState.Success(data)
                }
        }
    }

    fun getAllCustomers(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.getAllCustomers().catch { e -> _customer.value = ApiState.Failure(e) }
                .map { data -> data.filter { it.email == email } }.collect { data ->
                    _customer.value = ApiState.Success(data)
                }
        }
    }


    fun saveStringDS(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            repo.saveStringDS(key, value)
        }
    }

    fun getStringDS(key: Preferences.Key<String>) = repo.getStringDS(key)


}