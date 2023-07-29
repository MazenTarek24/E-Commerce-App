package com.mohamednader.shoponthego.Order.viewmodel

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OrderViewModel(val repository: Repository) : ViewModel() {
    private val _ordersList: MutableStateFlow<ApiState<List<Order>>> =
        MutableStateFlow(ApiState.Loading)

    val orderList: StateFlow<ApiState<List<Order>>> get() = _ordersList

    fun getAllOrders(customerID : Long) {
        viewModelScope.launch {
            repository.getAllOrders().catch { error ->
                _ordersList.value = ApiState.Failure(error)}
                    .map { data ->
                        data.filter {
                            try {
                                it.customer!!.id == customerID
                            } catch (e: Exception) {
                                false
                            }
                        }
                    }
                    .collect { data ->
                        _ordersList.value = ApiState.Success(data)
                    }
            }
        }


    fun getStringDS(key: Preferences.Key<String>) = repository.getStringDS(key)


}