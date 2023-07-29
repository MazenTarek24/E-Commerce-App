package com.mohamednader.shoponthego.Splash.ViewModel

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "SplashViewModel_INFO_TAG"

    fun saveStringDS(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            repo.saveStringDS(key, value)
        }
    }

    fun getStringDS(key: Preferences.Key<String>) = repo.getStringDS(key)
}