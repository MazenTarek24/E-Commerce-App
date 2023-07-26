package com.mohamednader.shoponthego.productinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.*
import com.mohamednader.shoponthego.Model.Pojo.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ViewModelProductInfo  (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _product: MutableStateFlow<ApiState<SingleProduct>> =
        MutableStateFlow<ApiState<SingleProduct>>(ApiState.Loading)
    val product : StateFlow<ApiState<SingleProduct>>
        get() = _product


    private var _productObject: MutableStateFlow<ApiState<Product>> =
        MutableStateFlow<ApiState<Product>>(ApiState.Loading)
    val productObject : StateFlow<ApiState<Product>>
        get() = _productObject

    private var _draft: MutableStateFlow<ApiState<List<DraftOrders>>> =
        MutableStateFlow<ApiState<List<DraftOrders>>>(ApiState.Loading)
    val drafts : StateFlow<ApiState<List<DraftOrders>>>
        get() = _draft


    private var _mdraft: MutableStateFlow<ApiState<DraftOrdermo>> =
        MutableStateFlow<ApiState<DraftOrdermo>>(ApiState.Loading)
    val modifydraft : StateFlow<ApiState<DraftOrdermo>>
        get() = _mdraft


    private var _draftWithId: MutableStateFlow<ApiState<DraftOrder>> =
        MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val draftwithid : StateFlow<ApiState<DraftOrder>>
        get() = _draftWithId

    private var _draftOrdersCartList: MutableStateFlow<ApiState<List<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>> =
        MutableStateFlow<ApiState<List<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>>(ApiState.Loading)
    val draftOrdersCartList: StateFlow<ApiState<List<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>>
        get() = _draftOrdersCartList


    private var _updatedDraftCartOrder: MutableStateFlow<ApiState<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>> =
        MutableStateFlow<ApiState<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>(ApiState.Loading)
    val updatedDraftCartOrder: StateFlow<ApiState<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>
        get() = _updatedDraftCartOrder


    private var _newDraftOrder: MutableStateFlow<ApiState<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>> =
        MutableStateFlow<ApiState<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>(ApiState.Loading)
    val newDraftOrder: StateFlow<ApiState<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>
        get() = _newDraftOrder

    fun getAllDraftsOrder(){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "get Drafts Orders: HomeViewModel")
            repo.getAllDraftsOrders()
                .catch { e -> _draft.value = ApiState.Failure(e)  }
                .collect{
                        data -> _draft.value = ApiState.Success(data)

                }
        }
    }

    fun modifyDraftsOrder(draftOrder: DraftOrderResponse, id: Long){
        viewModelScope.launch(Dispatchers.IO){
            println("is")
            Log.i(TAG, "get Drafts Orders: HomeViewModel")
            repo.modifyDraftforfav(draftOrder,id)
                .catch { e -> _mdraft.value = ApiState.Failure(e)  }
                .collect{
                        data -> _mdraft.value = ApiState.Success(data)

                }
        }
    }

    fun getProductByIdFromNetwork(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getProductByIdFromNetwork:  ViewModel")
            repo.getProductByID(productId)
                .catch { e -> _productObject.value = ApiState.Failure(e) }
                .collect { data ->
                    _productObject.value = ApiState.Success(data)
                }
        }
    }

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
    fun getDraftOrderWithId( id: Long){
        viewModelScope.launch(Dispatchers.IO){
            println("is"+id)
            Log.i(TAG, "get Drafts Orders: HomeViewModel")
            repo.getDraftWithId(id)
                .catch { e -> _draftWithId.value = ApiState.Failure(e)  }
                .collect{
                        data -> _draftWithId.value = ApiState.Success(data)

                }
        }
    }


    //Draft Orders
    fun getAllDraftOrdersCartFromNetwork(customerID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllDraftOrdersFromNetwork: HomeViewModel")
            repo.getAllDraftOrders()
                .catch { e -> _draftOrdersCartList.value = ApiState.Failure(e) }
                .map { data -> data.filter { it.customer!!.id == customerID && it.note == "cartDraft" } }
                .collect { data ->
                    _draftOrdersCartList.value = ApiState.Success(data)
                }
        }
    }


    fun updateDraftOrderCartOnNetwork(
            draftOrderId: Long,
            updatedDraftOrder: SingleDraftOrderResponse
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "updateDraftOrderOnNetwork: HomeViewModel")
            repo.updateDraftOrder(draftOrderId, updatedDraftOrder)
                .catch { e -> _updatedDraftCartOrder.value = ApiState.Failure(e) }
                .collect { data ->
                    _updatedDraftCartOrder.value = ApiState.Success(data)
                }
        }
    }

    fun addDraftOrderCartOnNetwork(
            newDraftOrder: SingleDraftOrderResponse
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "addDraftOrderOnNetwork: HomeViewModel")
            repo.addDraftOrder(newDraftOrder)
                .catch { e -> _newDraftOrder.value = ApiState.Failure(e) }
                .collect { data ->
                    _newDraftOrder.value = ApiState.Success(data)
                }
        }
    }


}