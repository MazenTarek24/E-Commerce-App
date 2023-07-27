package com.mohamednader.shoponthego.BranProduct.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.shoponthego.Model.Repo.Repository

class BrandProductViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrandProductViewModel::class.java)) {
            return BrandProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}