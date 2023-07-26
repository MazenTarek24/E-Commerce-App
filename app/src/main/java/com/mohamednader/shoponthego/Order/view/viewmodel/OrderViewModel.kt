package com.mohamednader.shoponthego.Order.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Model.order.OrderX
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrderViewModel(val repository: Repository) : ViewModel() {
    private val _ordersList: MutableStateFlow<ApiState<List<OrderX>>> =
        MutableStateFlow(ApiState.Loading)

    val orderList: StateFlow<ApiState<List<OrderX>>> get() = _ordersList

    fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders().catch { error ->
                _ordersList.value = ApiState.Failure(error)
            }.collect { result ->
                _ordersList.value = ApiState.Success(result)
            }
        }
    }
}