package com.mohamednader.shoponthego.Profile.ViewModel

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: RepositoryInterface) : ViewModel() {
    private val TAG = "ProfileViewModel_INFO_TAG"

    private val _ordersList: MutableStateFlow<ApiState<List<Order>>> =
        MutableStateFlow(ApiState.Loading)
    val orderList: StateFlow<ApiState<List<Order>>> get() = _ordersList

    private var _currencyRes: MutableStateFlow<ApiState<List<CurrencyInfo>>> =
        MutableStateFlow<ApiState<List<CurrencyInfo>>>(ApiState.Loading)
    val currencyRes: StateFlow<ApiState<List<CurrencyInfo>>>
        get() = _currencyRes

    private var _customer: MutableStateFlow<ApiState<Customer>> =
        MutableStateFlow<ApiState<Customer>>(ApiState.Loading)
    val customer: StateFlow<ApiState<Customer>>
        get() = _customer

    private var _order: MutableStateFlow<ApiState<Order>> =
        MutableStateFlow<ApiState<Order>>(ApiState.Loading)
    val order: StateFlow<ApiState<Order>>
        get() = _order

    private var _updateCustomer: MutableStateFlow<ApiState<Customer>> =
        MutableStateFlow<ApiState<Customer>>(ApiState.Loading)
    val updateCustomer: StateFlow<ApiState<Customer>>
        get() = _updateCustomer

    fun getAllCurrenciesFromNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCurrencyConvertor:  ViewModel")
            repo.getAllCurrencies().catch { e -> _currencyRes.value = ApiState.Failure(e) }
                .collect { data ->
                    _currencyRes.value = ApiState.Success(data)
                }
        }
    }

    fun getCustomerByIdFromNetwork(customerID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.getCustomerByID(customerID).catch { e -> _customer.value = ApiState.Failure(e) }
                .collect { data ->
                    _customer.value = ApiState.Success(data)
                }
        }
    }

    fun updateCustomerOnNetwork(customerId: Long, updatedCustomer: SingleCustomerResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "updateCustomerOnNetwork: HomeViewModel")
            repo.updateCustomer(customerId, updatedCustomer)
                .catch { e -> _updateCustomer.value = ApiState.Failure(e) }.collect { data ->
                    _updateCustomer.value = ApiState.Success(data)
                }
        }
    }

    fun deleteCustomerAddressOnNetwork(customerId: Long, addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "updateCustomerOnNetwork: HomeViewModel")
            repo.deleteUserAddress(customerId, addressId)
        }
    }

    fun getOrderByIdFromNetwork(orderID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.getOrderByID(orderID).catch { e -> _order.value = ApiState.Failure(e) }
                .collect { data ->
                    _order.value = ApiState.Success(data)
                }
        }
    }

    fun saveStringDS(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            repo.saveStringDS(key, value)
        }
    }

    fun getStringDS(key: Preferences.Key<String>) = repo.getStringDS(key)

    //To retrieve the all Currencies exchange Rate

    private var _currencyRate: MutableStateFlow<ApiState<List<ToCurrency>>> =
        MutableStateFlow<ApiState<List<ToCurrency>>>(ApiState.Loading)
    val currencyRate: StateFlow<ApiState<List<ToCurrency>>>
        get() = _currencyRate

    fun getCurrencyConvertorFromNetwork(from: String, to: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCurrencyConvertor:  ViewModel")
            repo.getCurrencyConvertor(from, to)
                .catch { e -> _currencyRate.value = ApiState.Failure(e) }
                .collect { data ->
                    _currencyRate.value = ApiState.Success(data)
                }
        }
    }

    fun getAllOrders(customerID : Long) {
        viewModelScope.launch {
            repo.getAllOrders().catch { error ->
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



}