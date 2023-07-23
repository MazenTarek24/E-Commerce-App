package com.mohamednader.shoponthego.Settings.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "SettingsViewModel_INFO_TAG"

    private var _currencyRes: MutableStateFlow<ApiState<List<CurrencyInfo>>> =
        MutableStateFlow<ApiState<List<CurrencyInfo>>>(ApiState.Loading)
    val currencyRes: StateFlow<ApiState<List<CurrencyInfo>>>
        get() = _currencyRes

    fun getAllCurrenciesFromNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCurrencyConvertor: HomeViewModel")
            repo.getAllCurrencies()
                .catch { e -> _currencyRes.value = ApiState.Failure(e) }
                .collect { data ->
                    _currencyRes.value = ApiState.Success(data)
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
            Log.i(TAG, "getCurrencyConvertor: HomeViewModel")
            repo.getCurrencyConvertor(from, to)
                .catch { e -> _currencyRes.value = ApiState.Failure(e) }
                .collect { data ->
                    _currencyRes.value = ApiState.Success(data)
                }
        }
    }
     */



}