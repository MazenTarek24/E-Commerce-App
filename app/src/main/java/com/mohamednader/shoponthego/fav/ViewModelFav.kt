package com.mohamednader.shoponthego.fav

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ViewModelFav(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _product: MutableStateFlow<ApiState<Product>> =
        MutableStateFlow<ApiState<Product>>(ApiState.Loading)
    val product: StateFlow<ApiState<Product>>
        get() = _product

    private var _draft: MutableStateFlow<ApiState<List<DraftOrder>>> =
        MutableStateFlow<ApiState<List<DraftOrder>>>(ApiState.Loading)
    val drafts: StateFlow<ApiState<List<DraftOrder>>>
        get() = _draft

    private var _mdraft: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val modifydraft: StateFlow<ApiState<DraftOrder>>
        get() = _mdraft

    private var _draftWithId: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val draftwithid: StateFlow<ApiState<DraftOrder>>
        get() = _draftWithId

    fun getAllDraftsOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "get Drafts Orders: HomeViewModel")
            repo.getAllDraftOrders().catch { e -> _draft.value = ApiState.Failure(e) }
                .collect { data ->
                    _draft.value = ApiState.Success(data)

                }
        }
    }

    fun modifyDraftsOrder(draftOrder: SingleDraftOrderResponse, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            println("is")
            Log.i(TAG, "get Drafts Orders: HomeViewModel")
            repo.modifyDraftforfav(draftOrder, id)
                .catch { e -> _mdraft.value = ApiState.Failure(e) }.collect { data ->
                    _mdraft.value = ApiState.Success(data)

                }
        }
    }

    fun deleteDraftOrderByID(draftOrderID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "deleteDraftOrderPending: HomeViewModel")
            repo.deleteDraftOrder(draftOrderID)
        }
    }

    fun getProductWithIdFromNetwork(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getProductWithIDFromNetwork: HomeViewModel")
            repo.getProductWithId(id).catch { e -> _product.value = ApiState.Failure(e) }
                .collect { data ->
                    _product.value = ApiState.Success(data)
                    println(data.vendor + "sssssssssssssssssss")
                }
        }
    }

    fun getDraftOrderWithId(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            println("is" + id)
            Log.i(TAG, "get Drafts Orders: HomeViewModel")
            repo.getDraftWithId(id).catch { e -> _draftWithId.value = ApiState.Failure(e) }
                .collect { data ->
                    _draftWithId.value = ApiState.Success(data)

                }
        }
    }

    fun getStringDS(key: Preferences.Key<String>) = repo.getStringDS(key)

}