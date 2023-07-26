package com.mohamednader.shoponthego.Profile.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: RepositoryInterface) : ViewModel() {
    private val TAG = "ProfileViewModel_INFO_TAG"

    private var _currencyRes: MutableStateFlow<ApiState<List<CurrencyInfo>>> =
        MutableStateFlow<ApiState<List<CurrencyInfo>>>(ApiState.Loading)
    val currencyRes: StateFlow<ApiState<List<CurrencyInfo>>>
        get() = _currencyRes

    private var _customer: MutableStateFlow<ApiState<Customer>> =
        MutableStateFlow<ApiState<Customer>>(ApiState.Loading)
    val customer: StateFlow<ApiState<Customer>>
        get() = _customer

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
                .catch { e -> _customer.value = ApiState.Failure(e) }.collect { data ->
                    _customer.value = ApiState.Success(data)
                }
        }
    }

    //To retrieve the all Currencies exchange Rate
    /*

    private var _currencyRes: MutableStateFlow<ApiState<List<ToCurrency>>> =
        MutableStateFlow<ApiState<List<ToCurrency>>>(ApiState.Loading)
    val currencyRes: StateFlow<ApiState<List<ToCurrency>>>
        get() = _currencyRes

    fun getCurrencyConvertorFromNetwork(from: String, to: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCurrencyConvertor:  ViewModel")
            repo.getCurrencyConvertor(from, to)
                .catch { e -> _currencyRes.value = ApiState.Failure(e) }
                .collect { data ->
                    _currencyRes.value = ApiState.Success(data)
                }
        }
    }
     */
}