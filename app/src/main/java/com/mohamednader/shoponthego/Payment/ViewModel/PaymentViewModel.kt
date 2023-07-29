package com.mohamednader.shoponthego.Payment.ViewModel

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
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

    private val TAG = "PaymentViewModel_INFO_TAG"

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

    private var _priceRulesList: MutableStateFlow<ApiState<List<PriceRules>>> =
        MutableStateFlow<ApiState<List<PriceRules>>>(ApiState.Loading)
    val priceRulesList: StateFlow<ApiState<List<PriceRules>>>
        get() = _priceRulesList

    private var _completeDraftOrderPending: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val completeDraftOrderPending: StateFlow<ApiState<DraftOrder>>
        get() = _completeDraftOrderPending

    private var _completeDraftOrderPaid: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val completeDraftOrderPaid: StateFlow<ApiState<DraftOrder>>
        get() = _completeDraftOrderPaid

    //Draft Orders
    fun getAllDraftOrdersFromNetwork(customerID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllDraftOrdersFromNetwork: HomeViewModel")
            repo.getAllDraftOrders().catch { e -> _draftOrdersList.value = ApiState.Failure(e) }
                .map { data ->
                    data.filter {
                        try {
                            it.customer!!.id == customerID && it.note == "cartDraft"
                        } catch (e: Exception) {
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

    fun getAllPriceRulesFromNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllPriceRulesFromNetwork: HomeViewModel")
            repo.getAllPriceRules()
                .catch { e -> _priceRulesList.value = ApiState.Failure(e) }
                .collect { data ->
                    _priceRulesList.value = ApiState.Success(data)
                }
        }
    }

    fun saveStringDS(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            repo.saveStringDS(key, value)
        }
    }

    fun getStringDS(key: Preferences.Key<String>) = repo.getStringDS(key)

    fun completeDraftOrderPendingByID(draftOrderID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "completeDraftOrderPending: HomeViewModel")
            repo.completeDraftOrderPending(draftOrderID, true)
                .catch { e -> _completeDraftOrderPending.value = ApiState.Failure(e) }
                .collect { data ->
                    _completeDraftOrderPending.value = ApiState.Success(data)
                }
        }
    }

    fun completeDraftOrderPaidByID(draftOrderID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "completeDraftOrderPending: HomeViewModel")
            repo.completeDraftOrderPaid(draftOrderID)
                .catch { e -> _completeDraftOrderPaid.value = ApiState.Failure(e) }
                .collect { data ->
                    _completeDraftOrderPaid.value = ApiState.Success(data)
                }
        }
    }

    fun deleteDraftOrderByID(draftOrderID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "deleteDraftOrderPending: HomeViewModel")
            repo.deleteDraftOrder(draftOrderID)
        }
    }

}