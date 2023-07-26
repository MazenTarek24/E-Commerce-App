package com.mohamednader.shoponthego.Cart.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class CartViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _draftOrdersList: MutableStateFlow<ApiState<List<DraftOrder>>> =
        MutableStateFlow<ApiState<List<DraftOrder>>>(ApiState.Loading)
    val draftOrdersList: StateFlow<ApiState<List<DraftOrder>>>
        get() = _draftOrdersList

    private var _updatedDraftCartOrder: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val updatedDraftCartOrder: StateFlow<ApiState<DraftOrder>>
        get() = _updatedDraftCartOrder

    //Draft Orders
    fun getAllDraftOrdersFromNetwork(customerID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllDraftOrdersFromNetwork: HomeViewModel")
            repo.getAllDraftOrders().catch { e -> _draftOrdersList.value = ApiState.Failure(e) }
                .map { data -> data.filter { it.customer!!.id == customerID && it.note == "cartDraft" } }
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

}