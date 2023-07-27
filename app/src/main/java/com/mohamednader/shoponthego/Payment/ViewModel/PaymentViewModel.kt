package com.mohamednader.shoponthego.Payment.ViewModel

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

class PaymentViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "SettingsViewModel_INFO_TAG"

    private var _draftOrdersList: MutableStateFlow<ApiState<List<DraftOrder>>> =
        MutableStateFlow<ApiState<List<DraftOrder>>>(ApiState.Loading)
    val draftOrdersList: StateFlow<ApiState<List<DraftOrder>>>
        get() = _draftOrdersList

    private var _updatedDraftCartOrder: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val updatedDraftCartOrder: StateFlow<ApiState<DraftOrder>>
        get() = _updatedDraftCartOrder

    private var _customer: MutableStateFlow<ApiState<Customer>> =
        MutableStateFlow<ApiState<Customer>>(ApiState.Loading)
    val customer: StateFlow<ApiState<Customer>>
        get() = _customer

    //Draft Orders
    fun getAllDraftOrdersFromNetwork(customerID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllDraftOrdersFromNetwork: HomeViewModel")
            repo.getAllDraftOrders().catch { e -> _draftOrdersList.value = ApiState.Failure(e) }
                .map { data ->
                    data.filter {
                        try {
                            it.customer!!.id == customerID && it.note == "cartDraft"
                        }catch (e: Exception){
                            false
                        }
                    }
                }
                .collect { data ->
                    _draftOrdersList.value = ApiState.Success(data)
                }
        }
    }

    fun updateDraftOrderCartOnNetwork(draftOrderId: Long,
                                      updatedDraftOrder: SingleDraftOrderResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "updateDraftOrderOnNetwork: HomeViewModel")
            repo.updateDraftOrder(draftOrderId, updatedDraftOrder)
                .catch { e -> _updatedDraftCartOrder.value = ApiState.Failure(e) }.collect { data ->
                    _updatedDraftCartOrder.value = ApiState.Success(data)
                }
        }
    }

    fun getCustomerByID(customerID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.getCustomerByID(customerID)
                .catch { e -> _customer.value = ApiState.Failure(e) }
                .collect { data ->
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