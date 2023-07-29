package com.mohamednader.shoponthego.Categories.ViewModel

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoriesViewModel(val repository: RepositoryInterface) : ViewModel() {
    private val TAG = "CategoryProductViewModel_INFO_TAG"

    private val _productCategory: MutableStateFlow<ApiState<List<Product>>> =
        MutableStateFlow(ApiState.Loading)
    val productCategory: StateFlow<ApiState<List<Product>>> get() = _productCategory

    private var _productsList: MutableStateFlow<ApiState<List<Product>>> =
        MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val productList: StateFlow<ApiState<List<Product>>>
        get() = _productsList

    fun getAllProductCategory(collectionId: Long, productType: String) {
        viewModelScope.launch(Dispatchers.IO) {
//            Log.i(TAG, "getAllCategoryProducts: ")
            repository.getAllProductCategory(collectionId, productType)
                .catch { error ->
                    _productCategory.value = ApiState.Failure(error)
                }.collect { result ->
                    _productCategory.value = ApiState.Success(result)

                }
        }
    }

    fun getStringDS(key: Preferences.Key<String>) = repository.getStringDS(key)

}