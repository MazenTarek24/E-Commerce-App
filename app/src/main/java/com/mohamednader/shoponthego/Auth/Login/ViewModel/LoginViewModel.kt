package com.mohamednader.shoponthego.Auth.Login.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.PostDraftOrder
import com.example.example.ResponseDraftOrderOb
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"

    private var _draftorder: MutableStateFlow<ApiState<ResponseDraftOrderOb>> =
        MutableStateFlow<ApiState<ResponseDraftOrderOb>>(ApiState.Loading)
    val customerList : StateFlow<ApiState<ResponseDraftOrderOb>>
        get() = _draftorder



    fun createDraftOrder(draftOrder: PostDraftOrder){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getAllProductsFromNetwork: HomeViewModel")
            repo.createDraftforfav(draftOrder)
                .catch { e -> _draftorder.value = ApiState.Failure(e) }
                .collect{ data -> _draftorder.value = ApiState.Success(data)
                }
        }
    }



}